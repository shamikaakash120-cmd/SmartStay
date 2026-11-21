package model;

/**
 * Enumeration representing the operational status of a hotel room.
 */
public enum E195569RoomStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    UNDER_MAINTENANCE("Under Maintenance");

    private final String displayName;

    E195569RoomStatus(String displayName) {
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
