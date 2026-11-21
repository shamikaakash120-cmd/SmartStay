package service;

import exception.E195569BookingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.E195569BedType;
import model.E195569Room;
import model.E195569RoomStatus;
import model.E195569RoomType;

/**
 * Service managing hotel room inventory with in-memory HashMap storage.
 */
public class E195569RoomService {

    // Data Structure: HashMap for O(1) average-time room lookups keyed by roomId
    private final Map<String, E195569Room> rooms = new HashMap<>();

    /**
     * Adds a new room to the repository, rejecting duplicate IDs.
     *
     * @param room The room to add.
     * @throws E195569BookingException if room is null or roomId already exists.
     */
    public void addRoom(E195569Room room) throws E195569BookingException {
        if (room == null) {
            throw new E195569BookingException("Cannot add room: Room reference cannot be null.");
        }
        String id = room.getRoomId();
        if (rooms.containsKey(id)) {
            throw new E195569BookingException("Cannot add room: Room ID '" + id + "' already exists.");
        }
        rooms.put(id, room);
    }

    /**
     * Updates the category, bed type, and status of an existing room.
     *
     * @param roomId   Room identifier.
     * @param roomType New room category.
     * @param bedType  New bed layout.
     * @param status   New operational status.
     * @throws E195569BookingException if roomId is unknown or any parameter is null.
     */
    public void updateRoom(String roomId, E195569RoomType roomType, E195569BedType bedType, E195569RoomStatus status)
            throws E195569BookingException {
        E195569Room room = findById(roomId);
        if (roomType == null || bedType == null || status == null) {
            throw new E195569BookingException("Room attributes (roomType, bedType, status) cannot be null.");
        }
        room.setRoomType(roomType);
        room.setBedType(bedType);
        room.setStatus(status);
    }

    /**
     * Removes an existing room from the inventory.
     *
     * @param roomId Room identifier.
     * @throws E195569BookingException if roomId is not found.
     */
    public void removeRoom(String roomId) throws E195569BookingException {
        if (roomId == null || !rooms.containsKey(roomId.trim())) {
            throw new E195569BookingException("Cannot remove room: Room ID '" + roomId + "' not found.");
        }
        rooms.remove(roomId.trim());
    }

    /**
     * Finds a room by its unique room ID.
     *
     * @param roomId Room identifier.
     * @return Found room instance.
     * @throws E195569BookingException if roomId does not exist.
     */
    public E195569Room findById(String roomId) throws E195569BookingException {
        if (roomId == null || !rooms.containsKey(roomId.trim())) {
            throw new E195569BookingException("Room with ID '" + roomId + "' was not found.");
        }
        return rooms.get(roomId.trim());
    }

    /**
     * Returns an unmodifiable list of all rooms sorted by roomId.
     *
     * @return List of all rooms sorted naturally by roomId.
     */
    // Algorithm: Comparison-based sorting on roomId
    public List<E195569Room> getAllRooms() {
        List<E195569Room> sortedList = new ArrayList<>(rooms.values());
        sortedList.sort(Comparator.comparing(E195569Room::getRoomId));
        return sortedList;
    }

    /**
     * Updates the operational status of an existing room.
     *
     * @param roomId Room identifier.
     * @param status New status to apply.
     * @throws E195569BookingException if room does not exist or status is null.
     */
    public void updateStatus(String roomId, E195569RoomStatus status) throws E195569BookingException {
        if (status == null) {
            throw new E195569BookingException("Room status cannot be null.");
        }
        E195569Room room = findById(roomId);
        room.setStatus(status);
    }

    /**
     * Searches and returns all rooms of a given type whose status is AVAILABLE.
     *
     * @param type Desired room category.
     * @return List of matching available rooms.
     */
    // Algorithm: Linear Filter matching roomType and AVAILABLE status
    public List<E195569Room> searchAvailable(E195569RoomType type) {
        List<E195569Room> result = new ArrayList<>();
        for (E195569Room r : rooms.values()) {
            if (r.getRoomType() == type && r.isAvailable()) {
                result.add(r);
            }
        }
        result.sort(Comparator.comparing(E195569Room::getRoomId));
        return result;
    }
}
