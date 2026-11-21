package model;

/**
 * Enumeration representing the valid lifecycle states of a hotel reservation.
 * Restricted to CONFIRMED and CANCELLED as per coursework rules.
 */
public enum E195569BookingStatus {
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled");

    private final String displayName;

    E195569BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
