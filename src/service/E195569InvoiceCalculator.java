package service;

import java.util.Objects;
import model.E195569Booking;
import model.E195569Invoice;
import model.E195569Room;
import model.E195569RoomType;

/**
 * Service calculating itemized invoice totals for reservations according to coursework rules.
 */
public class E195569InvoiceCalculator {

    /** Standard refundable booking advance payment required at reservation. */
    public static final double ADVANCE = 10000.0;

    /** Discount percentage (12%) applied to base room charge for stays >= 5 nights. */
    public static final double DISCOUNT_RATE = 0.12;

    /** Minimum stay duration in nights required to qualify for long stay discount. */
    public static final int DISCOUNT_THRESHOLD_NIGHTS = 5;

    /**
     * Calculates the itemized invoice for a given booking.
     * Enforces the exact formula:
     * - base = dailyRate x nights
     * - extra = max(0, guests - capacity) x extraGuestCharge x nights
     * - discount = nights >= 5 ? DISCOUNT_RATE x base : 0
     * - taxable = base - discount + extra
     * - tax = taxable x taxRate
     * - final = taxable + tax - ADVANCE
     *
     * @param booking The booking for which to calculate the invoice.
     * @return Fully populated E195569Invoice instance.
     */
    // Algorithm: Direct calculation applying hotel billing rules and tiered long-stay discount
    public E195569Invoice calculate(E195569Booking booking) {
        Objects.requireNonNull(booking, "Booking cannot be null.");

        E195569Room room = booking.getRoom();
        E195569RoomType roomType = room.getRoomType();

        double dailyRate = room.getDailyRate();
        long nights = booking.getNights();
        int guests = booking.getNumberOfGuests();
        int capacity = roomType.getCapacity();
        double extraGuestCharge = roomType.getExtraGuestCharge();
        double taxRate = roomType.getTaxRate();
        boolean breakfastIncluded = roomType.isBreakfastIncluded();

        // Business formula implementation
        double base = dailyRate * nights;
        double extra = Math.max(0, guests - capacity) * extraGuestCharge * nights;
        double discount = nights >= DISCOUNT_THRESHOLD_NIGHTS ? DISCOUNT_RATE * base : 0;
        double taxable = base - discount + extra;
        double tax = taxable * taxRate;
        double finalAmount = taxable + tax - ADVANCE;

        return new E195569Invoice(
                booking.getGuest(), room, booking.getCheckInDate(), booking.getCheckOutDate(),
                nights, guests, breakfastIncluded, base, extra, discount, tax, ADVANCE, finalAmount
        );
    }
}
