package model;

import java.util.Objects;

/**
 * Represents a hotel room in the SmartStay system.
 * Encapsulates room identification, category, bed layout, and operational status.
 */
public class E195569Room {
    private final String roomId;
    private E195569RoomType roomType;
    private E195569BedType bedType;
    private E195569RoomStatus status;

    /**
     * Constructs a Room instance with validated attributes.
     *
     * @param roomId   Unique room identifier (e.g., "101", "205").
     * @param roomType Room category defining rate, breakfast, tax, and capacity.
     * @param bedType  Bed layout configuration.
     * @param status   Operational status (AVAILABLE, RESERVED, UNDER_MAINTENANCE).
     * @throws IllegalArgumentException if roomId is null/blank, or enum references are null.
     */
    public E195569Room(String roomId, E195569RoomType roomType, E195569BedType bedType, E195569RoomStatus status) {
        if (roomId == null || roomId.isBlank()) {
            throw new IllegalArgumentException("Room ID cannot be null or blank.");
        }
        this.roomId = roomId.trim();
        this.roomType = Objects.requireNonNull(roomType, "Room type cannot be null.");
        this.bedType = Objects.requireNonNull(bedType, "Bed type cannot be null.");
        this.status = Objects.requireNonNull(status, "Room status cannot be null.");
    }

    public String getRoomId() {
        return roomId;
    }

    public E195569RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(E195569RoomType roomType) {
        this.roomType = Objects.requireNonNull(roomType, "Room type cannot be null.");
    }

    public E195569BedType getBedType() {
        return bedType;
    }

    public void setBedType(E195569BedType bedType) {
        this.bedType = Objects.requireNonNull(bedType, "Bed type cannot be null.");
    }

    public E195569RoomStatus getStatus() {
        return status;
    }

    /**
     * Checks if the room is currently available for guest booking.
     *
     * @return true if room status is AVAILABLE, false otherwise.
     */
    public boolean isAvailable() {
        return this.status == E195569RoomStatus.AVAILABLE;
    }

    /**
     * Returns the daily rate of the room by delegating to its room type.
     *
     * @return Daily room charge in LKR.
     */
    public double getDailyRate() {
        return roomType.getDailyRate();
    }

    /**
     * Updates the operational state of the room.
     *
     * @param status The new status to transition to.
     */
    public void setStatus(E195569RoomStatus status) {
        this.status = Objects.requireNonNull(status, "Room status cannot be null.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        E195569Room room = (E195569Room) o;
        return Objects.equals(roomId, room.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-14s | %-12s | %-18s | LKR %,9.2f |",
                roomId, roomType.getDisplayName(), bedType.getDisplayName(),
                status.getDisplayName(), getDailyRate());
    }
}
