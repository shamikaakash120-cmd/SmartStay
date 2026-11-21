package model;

/**
 * Enumeration representing predefined room types and their fixed business specifications.
 * Enforces daily rate, breakfast eligibility, extra guest surcharge, tax rate, and standard capacity.
 */
public enum E195569RoomType {
    STANDARD("Standard Room", 6000.0, false, 1000.0, 0.10, 2),
    DELUXE("Deluxe Room", 9000.0, true, 1500.0, 0.12, 3),
    SUITE("Suite", 14000.0, true, 2000.0, 0.15, 4),
    FAMILY_SUITE("Family Suite", 18000.0, true, 2500.0, 0.15, 6);

    private final String displayName;
    private final double dailyRate;
    private final boolean breakfastIncluded;
    private final double extraGuestCharge;
    private final double taxRate;
    private final int capacity;

    E195569RoomType(String displayName, double dailyRate, boolean breakfastIncluded,
                    double extraGuestCharge, double taxRate, int capacity) {
        this.displayName = displayName;
        this.dailyRate = dailyRate;
        this.breakfastIncluded = breakfastIncluded;
        this.extraGuestCharge = extraGuestCharge;
        this.taxRate = taxRate;
        this.capacity = capacity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public double getExtraGuestCharge() {
        return extraGuestCharge;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return String.format("%s (LKR %,.2f/night, Capacity: %d, Breakfast: %s)",
                displayName, dailyRate, capacity, breakfastIncluded ? "Included" : "None");
    }
}
