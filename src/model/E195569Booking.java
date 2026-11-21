package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a reservation record in the SmartStay system.
 * Connects the guest, assigned room, stay duration, advance payment, and reservation lifecycle state.
 */
public class E195569Booking {

    /** Standard refundable booking advance payment required at reservation (LKR 10,000). */
    private static final double STANDARD_ADVANCE = 10000.0;

    /** Window in hours prior to check-in midnight when cancellation/modification is blocked. */
    private static final int CANCELLATION_DEADLINE_HOURS = 24;

    /** Minimum required number of guests for any valid booking. */
    private static final int MIN_GUESTS = 1;

    private final String bookingId;
    private final E195569Guest guest;
    private final E195569Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final int numberOfGuests;
    private final double advancePaid;
    private E195569BookingStatus bookingStatus;

    /**
     * Constructs a validated booking instance with standard advance payment of LKR 10,000.
     *
     * @param bookingId      Unique booking identifier (e.g., "BK-1001").
     * @param guest          Registered guest holding the reservation.
     * @param room           Allocated room for the stay.
     * @param checkInDate    Start date of the reservation.
     * @param checkOutDate   End date of the reservation.
     * @param numberOfGuests Number of occupants staying in the room.
     * @throws IllegalArgumentException if arguments are null/blank, guests < 1, or check-out is not after check-in.
     */
    public E195569Booking(String bookingId, E195569Guest guest, E195569Room room,
                          LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        this(bookingId, guest, room, checkInDate, checkOutDate, numberOfGuests, E195569BookingStatus.CONFIRMED);
    }

    /**
     * Overloaded constructor supporting initial status specification.
     */
    public E195569Booking(String bookingId, E195569Guest guest, E195569Room room,
                          LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests,
                          E195569BookingStatus bookingStatus) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID cannot be null or blank.");
        }
        this.guest = Objects.requireNonNull(guest, "Guest cannot be null.");
        this.room = Objects.requireNonNull(room, "Room cannot be null.");
        this.checkInDate = Objects.requireNonNull(checkInDate, "Check-in date cannot be null.");
        this.checkOutDate = Objects.requireNonNull(checkOutDate, "Check-out date cannot be null.");

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date (" + checkOutDate + ") must be after check-in date (" + checkInDate + ").");
        }
        if (numberOfGuests < MIN_GUESTS) {
            throw new IllegalArgumentException("Number of guests must be at least " + MIN_GUESTS + ", but received " + numberOfGuests + ".");
        }

        this.bookingId = bookingId.trim();
        this.numberOfGuests = numberOfGuests;
        this.advancePaid = STANDARD_ADVANCE;
        this.bookingStatus = Objects.requireNonNull(bookingStatus, "Booking status cannot be null.");
    }

    public String getBookingId() {
        return bookingId;
    }

    public E195569Guest getGuest() {
        return guest;
    }

    public E195569Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public double getAdvancePaid() {
        return advancePaid;
    }

    public E195569BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    /**
     * Calculates the total duration of the stay in nights.
     *
     * @return Number of nights between check-in and check-out dates.
     */
    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    /**
     * Determines whether the reservation can be modified or cancelled based on business rules.
     * Allowed only if status is CONFIRMED and more than 24 hours remain before check-in at 00:00.
     *
     * @param now Current date and time reference.
     * @return true if cancellation or modification is permitted; false otherwise.
     */
    public boolean canModifyOrCancel(LocalDateTime now) {
        if (bookingStatus != E195569BookingStatus.CONFIRMED) {
            return false;
        }
        LocalDateTime checkInMidnight = checkInDate.atStartOfDay();
        return now.isBefore(checkInMidnight.minusHours(CANCELLATION_DEADLINE_HOURS));
    }

    /**
     * Checks if a candidate date range conflicts with this booking's date interval.
     *
     * @param in  Candidate check-in date.
     * @param out Candidate check-out date.
     * @return true if the date ranges overlap, false otherwise.
     */
    // Algorithm: Interval Overlap Detection - two half-open intervals [in, out) and [checkInDate, checkOutDate) overlap if in < checkOutDate && out > checkInDate
    public boolean overlaps(LocalDate in, LocalDate out) {
        if (in == null || out == null || !out.isAfter(in)) {
            throw new IllegalArgumentException("Invalid date range supplied for overlap check.");
        }
        return in.isBefore(checkOutDate) && out.isAfter(checkInDate);
    }

    /**
     * Transitions the reservation status to CANCELLED.
     */
    public void cancel() {
        this.bookingStatus = E195569BookingStatus.CANCELLED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        E195569Booking that = (E195569Booking) o;
        return Objects.equals(bookingId, that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    @Override
    public String toString() {
        return String.format("| %-9s | %-7s | %-16s | %-10s | %-10s | %2d night(s) | %2d guest(s) | LKR %,9.2f | %-9s |",
                bookingId, room.getRoomId(), guest.getFullName(), checkInDate, checkOutDate,
                getNights(), numberOfGuests, advancePaid, bookingStatus.getDisplayName());
    }
}
