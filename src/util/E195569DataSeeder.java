package util;

import exception.E195569BookingException;
import java.util.List;
import model.E195569Admin;
import model.E195569BedType;
import model.E195569Guest;
import model.E195569Room;
import model.E195569RoomStatus;
import model.E195569RoomType;
import service.E195569GuestService;
import service.E195569RoomService;

/**
 * Utility responsible for pre-populating realistic demonstration data.
 * Facilitates coursework demonstration and test execution using only model and service constructors.
 */
public final class E195569DataSeeder {

    private E195569DataSeeder() {}

    /**
     * Seeds initial rooms, guests, and administrator into their respective services/stores.
     *
     * @param roomService  Target room management service.
     * @param guestService Target guest management service.
     * @param adminStore   List holding administrative accounts.
     */
    public static void seed(E195569RoomService roomService, E195569GuestService guestService,
                            List<E195569Admin> adminStore) {
        seedRooms(roomService);
        seedGuests(guestService);
        seedAdmins(adminStore);
    }

    /**
     * Loads 8 sample rooms, two of each type with mixed bed types (IDs R101 to R108).
     * All seeded rooms start with AVAILABLE status.
     *
     * @param roomService Target room service.
     */
    private static void seedRooms(E195569RoomService roomService) {
        if (roomService == null) {
            return;
        }
        try {
            // Standard Rooms (2)
            roomService.addRoom(new E195569Room("R101", E195569RoomType.STANDARD, E195569BedType.SINGLE, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("R102", E195569RoomType.STANDARD, E195569BedType.DOUBLE, E195569RoomStatus.AVAILABLE));

            // Deluxe Rooms (2)
            roomService.addRoom(new E195569Room("R103", E195569RoomType.DELUXE, E195569BedType.DOUBLE, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("R104", E195569RoomType.DELUXE, E195569BedType.QUEEN, E195569RoomStatus.AVAILABLE));

            // Suite Rooms (2)
            roomService.addRoom(new E195569Room("R105", E195569RoomType.SUITE, E195569BedType.QUEEN, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("R106", E195569RoomType.SUITE, E195569BedType.KING, E195569RoomStatus.AVAILABLE));

            // Family Suite Rooms (2)
            roomService.addRoom(new E195569Room("R107", E195569RoomType.FAMILY_SUITE, E195569BedType.KING, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("R108", E195569RoomType.FAMILY_SUITE, E195569BedType.QUEEN, E195569RoomStatus.AVAILABLE));
        } catch (E195569BookingException e) {
            System.err.println("Notice: Failed to seed room: " + e.getMessage());
        }
    }

    /**
     * Loads 2 sample guests with valid NIC, name, contact number, and email.
     *
     * @param guestService Target guest service.
     */
    private static void seedGuests(E195569GuestService guestService) {
        if (guestService == null) {
            return;
        }
        try {
            guestService.registerGuest(new E195569Guest("200012345678", "Nimal Silva", "+94771234567", "nimal.silva@example.com"));
            guestService.registerGuest(new E195569Guest("N98765432", "Sarah Jenkins", "+94765432100", "sarah.j@travelworld.com"));
        } catch (E195569BookingException e) {
            System.err.println("Notice: Failed to seed guest: " + e.getMessage());
        }
    }

    /**
     * Loads one Admin account with PIN 1234.
     *
     * @param adminStore Target administrator collection.
     */
    private static void seedAdmins(List<E195569Admin> adminStore) {
        if (adminStore != null) {
            adminStore.add(new E195569Admin("ADM01", "1234", "System Administrator", "+94112345678", "admin@smartstay.lk"));
        }
    }
}
