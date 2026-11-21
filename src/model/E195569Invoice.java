package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an itemized financial invoice for a hotel reservation.
 * Encapsulates stay parameters, guest/room associations, and itemized billing components.
 */
public class E195569Invoice {
    private static final int MIN_NIGHTS = 1;
    private static final int MIN_GUESTS = 1;
    private static final double PERCENTAGE_FACTOR = 100.0;

    private final E195569Guest guest;
    private final E195569Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final long nights;
    private final int numberOfGuests;
    private final boolean breakfastIncluded;
    private final double baseCharge;
    private final double extraGuestCharge;
    private final double discount;
    private final double tax;
    private final double advanceDeducted;
    private final double finalAmount;

    public E195569Invoice(E195569Guest guest, E195569Room room, LocalDate checkInDate, LocalDate checkOutDate,
                          long nights, int numberOfGuests, boolean breakfastIncluded, double baseCharge,
                          double extraGuestCharge, double discount, double tax, double advanceDeducted, double finalAmount) {
        this.guest = Objects.requireNonNull(guest, "Guest cannot be null.");
        this.room = Objects.requireNonNull(room, "Room cannot be null.");
        this.checkInDate = Objects.requireNonNull(checkInDate, "Check-in date cannot be null.");
        this.checkOutDate = Objects.requireNonNull(checkOutDate, "Check-out date cannot be null.");
        if (nights < MIN_NIGHTS) {
            throw new IllegalArgumentException("Nights must be at least " + MIN_NIGHTS + ".");
        }
        if (numberOfGuests < MIN_GUESTS) {
            throw new IllegalArgumentException("Number of guests must be at least " + MIN_GUESTS + ".");
        }

        this.nights = nights;
        this.numberOfGuests = numberOfGuests;
        this.breakfastIncluded = breakfastIncluded;
        this.baseCharge = baseCharge;
        this.extraGuestCharge = extraGuestCharge;
        this.discount = discount;
        this.tax = tax;
        this.advanceDeducted = advanceDeducted;
        this.finalAmount = finalAmount;
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

    public long getNights() {
        return nights;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public double getBaseCharge() {
        return baseCharge;
    }

    public double getExtraGuestCharge() {
        return extraGuestCharge;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTax() {
        return tax;
    }

    public double getAdvanceDeducted() {
        return advanceDeducted;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    /**
     * Prints a neatly formatted invoice displaying all parameters and billing components.
     * Aligns every label colon uniformly and dynamically presents tax rate and guest details.
     */
    public void print() {
        int taxPercentage = (int) Math.round(room.getRoomType().getTaxRate() * PERCENTAGE_FACTOR);
        String taxLabel = String.format("Tax Added (%d%%)", taxPercentage);

        System.out.println("================================================================================");
        System.out.println("                      SMARTSTAY HOTEL BOOKING INVOICE                           ");
        System.out.println("================================================================================");
        System.out.printf(" %-21s: %s%n", "Room ID", room.getRoomId());
        System.out.printf(" %-21s: %s%n", "Room Type", room.getRoomType().getDisplayName());
        System.out.printf(" %-21s: %s%n", "Bed Type", room.getBedType().getDisplayName());
        System.out.printf(" %-21s: LKR %,.2f%n", "Daily Rate", room.getDailyRate());
        System.out.printf(" %-21s: %s%n", "Breakfast Included", breakfastIncluded ? "Yes" : "No");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf(" %-21s: %s%n", "NIC/Passport", guest.getNicOrPassport());
        System.out.printf(" %-21s: %s%n", "Full Name", guest.getFullName());
        System.out.printf(" %-21s: %s%n", "Contact", guest.getContactNumber());
        System.out.printf(" %-21s: %s%n", "Email", guest.getEmail());
        System.out.printf(" %-21s: %s%n", "Check-In Date", checkInDate);
        System.out.printf(" %-21s: %s%n", "Check-Out Date", checkOutDate);
        System.out.printf(" %-21s: %d%n", "Number of Nights", nights);
        System.out.printf(" %-21s: %d (Base Capacity: %d)%n", "Number of Guests", numberOfGuests, room.getRoomType().getCapacity());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf(" %-21s: LKR %,12.2f%n", "Base Room Charge", baseCharge);
        System.out.printf(" %-21s: LKR %,12.2f%n", "Extra Guest Charges", extraGuestCharge);
        System.out.printf(" %-21s: -LKR %,11.2f%n", "Discount Applied", discount);
        System.out.printf(" %-21s: +LKR %,11.2f%n", taxLabel, tax);
        System.out.printf(" %-21s: -LKR %,11.2f%n", "Booking Advance Paid", advanceDeducted);
        System.out.println("================================================================================");
        System.out.printf(" %-21s: LKR %,12.2f%n", "Final Payable Amount", finalAmount);
        System.out.println("================================================================================");
    }
}
