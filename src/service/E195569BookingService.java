package service;

import exception.E195569BookingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import model.E195569Booking;
import model.E195569BookingStatus;
import model.E195569Guest;
import model.E195569Invoice;
import model.E195569Room;
import model.E195569RoomStatus;
import model.E195569RoomType;

/**
 * Service managing reservation lifecycles, availability, searching, and invoice generation.
 */
public class E195569BookingService {

    /** Minimum lead time in days required between booking creation and check-in date. */
    private static final int MIN_LEAD_TIME_DAYS = 2;

    /** Minimum required number of guests for any valid booking. */
    private static final int MIN_GUESTS = 1;

    // Data Structure: ArrayList holding the in-memory sequence of all booking records
    private final List<E195569Booking> bookings = new ArrayList<>();

    private final E195569RoomService roomService;
    private final E195569GuestService guestService;
    private final E195569InvoiceCalculator invoiceCalculator;
    private int bookingCounter = 1;

    public E195569BookingService(E195569RoomService roomService, E195569GuestService guestService,
                                 E195569InvoiceCalculator invoiceCalculator) {
        this.roomService = Objects.requireNonNull(roomService, "Room service cannot be null.");
        this.guestService = Objects.requireNonNull(guestService, "Guest service cannot be null.");
        this.invoiceCalculator = Objects.requireNonNull(invoiceCalculator, "Invoice calculator cannot be null.");
    }

    /**
     * Creates a new booking by locating the first available room of the requested type without overlapping reservations.
     *
     * @param nicOrPassport  Guest identifier.
     * @param roomType       Requested room category.
     * @param numberOfGuests Number of occupants.
     * @param checkIn        Reservation start date.
     * @param checkOut       Reservation end date.
     * @return Newly created and stored E195569Booking instance.
     * @throws E195569BookingException if any business rule is violated or no room is free.
     */
    // Algorithm: First-fit linear search combined with interval overlap detection
    public E195569Booking createBooking(String nicOrPassport, E195569RoomType roomType, int numberOfGuests,
                                        LocalDate checkIn, LocalDate checkOut) throws E195569BookingException {
        E195569Guest guest = guestService.findByNic(nicOrPassport);
        if (numberOfGuests < MIN_GUESTS) {
            throw new E195569BookingException("Number of guests must be at least " + MIN_GUESTS + ", but received " + numberOfGuests + ".");
        }
        validateDateRange(checkIn, checkOut);
        LocalDate earliestAllowed = LocalDate.now().plusDays(MIN_LEAD_TIME_DAYS);
        if (checkIn.isBefore(earliestAllowed)) {
            throw new E195569BookingException("Check-in must be at least 2 days after today (" + earliestAllowed + " onwards).");
        }
        if (roomType == null) {
            throw new E195569BookingException("Room type cannot be null.");
        }

        E195569Room assignedRoom = null;
        for (E195569Room r : roomService.searchAvailable(roomType)) {
            if (!hasConflictingBooking(r, checkIn, checkOut)) {
                assignedRoom = r;
                break;
            }
        }

        if (assignedRoom == null) {
            throw new E195569BookingException("No available room of type " + roomType.getDisplayName() + " for dates " + checkIn + " to " + checkOut + ".");
        }

        String bookingId = String.format("BK-%04d", bookingCounter++);
        E195569Booking booking = new E195569Booking(bookingId, guest, assignedRoom, checkIn, checkOut, numberOfGuests);
        assignedRoom.setStatus(E195569RoomStatus.RESERVED);
        bookings.add(booking);
        return booking;
    }

    /**
     * Searches for all rooms of a given type with status AVAILABLE and no overlapping CONFIRMED bookings.
     *
     * @param type     Room category.
     * @param checkIn  Desired check-in date.
     * @param checkOut Desired check-out date.
     * @return List of matching available rooms.
     * @throws E195569BookingException if check-out date is not after check-in date or dates are null.
     */
    // Algorithm: Linear filtering with interval overlap conflict detection
    public List<E195569Room> searchAvailableRooms(E195569RoomType type, LocalDate checkIn, LocalDate checkOut)
            throws E195569BookingException {
        validateDateRange(checkIn, checkOut);
        if (type == null) {
            throw new E195569BookingException("Room type cannot be null.");
        }

        List<E195569Room> available = new ArrayList<>();
        for (E195569Room r : roomService.searchAvailable(type)) {
            if (!hasConflictingBooking(r, checkIn, checkOut)) {
                available.add(r);
            }
        }
        return Collections.unmodifiableList(available);
    }

    /**
     * Cancels an existing booking if outside the 24-hour check-in window.
     *
     * @param bookingId Booking identifier.
     * @return Cancelled booking instance.
     * @throws E195569BookingException if not found or within 24 hours of check-in at 00:00.
     */
    // Algorithm: Linear search for target booking and state transition
    public E195569Booking cancelBooking(String bookingId) throws E195569BookingException {
        E195569Booking booking = searchByBookingId(bookingId);
        if (!booking.canModifyOrCancel(LocalDateTime.now())) {
            throw new E195569BookingException("Cancellation rejected: Cannot cancel within 24 hours of check-in at 00:00 (Rule: >24h required).");
        }

        booking.cancel();
        booking.getRoom().setStatus(E195569RoomStatus.AVAILABLE);
        return booking;
    }

    /**
     * Searches for a booking by its exact ID using linear search.
     *
     * @param id Booking identifier.
     * @return Matching booking.
     * @throws E195569BookingException if not found.
     */
    // Algorithm: Linear Search for exact-key match
    public E195569Booking searchByBookingId(String id) throws E195569BookingException {
        if (id == null || id.isBlank()) {
            throw new E195569BookingException("Booking ID cannot be blank.");
        }
        for (E195569Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(id.trim())) {
                return b;
            }
        }
        throw new E195569BookingException("Booking with ID '" + id + "' was not found.");
    }

    /**
     * Searches bookings matching guest name partially (case-insensitive).
     *
     * @param name Guest name or substring.
     * @return List of matching bookings.
     */
    // Algorithm: Linear Search with case-insensitive partial substring match
    public List<E195569Booking> searchByGuestName(String name) {
        if (name == null || name.isBlank()) {
            return Collections.emptyList();
        }
        String query = name.trim().toLowerCase();
        List<E195569Booking> matching = new ArrayList<>();
        for (E195569Booking b : bookings) {
            if (b.getGuest().getFullName().toLowerCase().contains(query)) {
                matching.add(b);
            }
        }
        return Collections.unmodifiableList(matching);
    }

    /**
     * Returns a copy of all bookings sorted chronologically by check-in date.
     *
     * @return Sorted copy of bookings list.
     */
    // Algorithm: Comparator-based TimSort on checkInDate
    public List<E195569Booking> viewByCheckInDate() {
        List<E195569Booking> sorted = new ArrayList<>(bookings);
        sorted.sort(Comparator.comparing(E195569Booking::getCheckInDate));
        return Collections.unmodifiableList(sorted);
    }

    /**
     * Generates an itemized invoice for the requested booking.
     *
     * @param bookingId Booking identifier.
     * @return E195569Invoice instance computed by E195569InvoiceCalculator.
     * @throws E195569BookingException if booking is not found.
     */
    // Algorithm: Linear search for booking ID followed by direct formula calculation
    public E195569Invoice generateInvoice(String bookingId) throws E195569BookingException {
        E195569Booking booking = searchByBookingId(bookingId);
        return invoiceCalculator.calculate(booking);
    }

    /**
     * Helper checking for date collisions on a room against active confirmed bookings.
     */
    // Algorithm: Interval Overlap Detection (in < otherOut && out > otherIn)
    private boolean hasConflictingBooking(E195569Room room, LocalDate in, LocalDate out) {
        for (E195569Booking b : bookings) {
            if (b.getRoom().equals(room) && b.getBookingStatus() == E195569BookingStatus.CONFIRMED) {
                if (b.overlaps(in, out)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates that dates are non-null and check-out is strictly after check-in.
     */
    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) throws E195569BookingException {
        if (checkIn == null || checkOut == null) {
            throw new E195569BookingException("Check-in and check-out dates cannot be null.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new E195569BookingException("Check-out date (" + checkOut + ") must be after check-in date (" + checkIn + ").");
        }
    }
}
