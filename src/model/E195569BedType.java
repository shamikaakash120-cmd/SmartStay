package model;

/**
 * Enumeration representing the available bed configurations for hotel rooms.
 */
public enum E195569BedType {
    SINGLE("Single Bed"),
    DOUBLE("Double Bed"),
    QUEEN("Queen Bed"),
    KING("King Bed");

    private final String displayName;

    E195569BedType(String displayName) {
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
