package ui;

import exception.E195569BookingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import model.E195569Admin;
import model.E195569BedType;
import model.E195569Booking;
import model.E195569Guest;
import model.E195569Invoice;
import model.E195569Room;
import model.E195569RoomStatus;
import model.E195569RoomType;
import service.E195569BookingService;
import service.E195569GuestService;
import service.E195569InvoiceCalculator;
import service.E195569RoomService;
import util.E195569ConsoleReader;

/**
 * Console user interface coordinating input and presentation for the SmartStay system.
 * Delegating all business and domain validation to underlying service components.
 */
public class E195569ConsoleMenu {

    private static final String DIVIDER = "--------------------------------------------------------------------------------";
    private static final String BANNER_BORDER = "================================================================================";

    private static final int MAX_PIN_ATTEMPTS = 3;
    private static final int MIN_GUESTS = 1;
    private static final int MAX_GUESTS = 10;
    private static final int MAIN_MENU_MIN = 1;
    private static final int MAIN_MENU_MAX = 3;
    private static final int ADMIN_MENU_MIN = 1;
    private static final int ADMIN_MENU_MAX = 6;
    private static final int GUEST_MENU_MIN = 1;
    private static final int GUEST_MENU_MAX = 8;
    private static final int SEARCH_CHOICE_MIN = 1;
    private static final int SEARCH_CHOICE_MAX = 2;

    private final E195569RoomService roomService;
    private final E195569GuestService guestService;
    private final E195569BookingService bookingService;
    private final E195569InvoiceCalculator invoiceCalculator;
    private final List<E195569Admin> admins;

    /**
     * Constructs the console menu with all necessary service dependencies and admin store.
     *
     * @param roomService       Room inventory management service.
     * @param guestService      Guest registration and lookup service.
     * @param bookingService    Reservation lifecycle service.
     * @param invoiceCalculator Billing and tax calculator.
     * @param admins            Administrative account repository.
     */
    public E195569ConsoleMenu(E195569RoomService roomService, E195569GuestService guestService,
                              E195569BookingService bookingService, E195569InvoiceCalculator invoiceCalculator,
                              List<E195569Admin> admins) {
        this.roomService = Objects.requireNonNull(roomService, "Room service cannot be null.");
        this.guestService = Objects.requireNonNull(guestService, "Guest service cannot be null.");
        this.bookingService = Objects.requireNonNull(bookingService, "Booking service cannot be null.");
        this.invoiceCalculator = invoiceCalculator != null ? invoiceCalculator : new E195569InvoiceCalculator();
        this.admins = Objects.requireNonNull(admins, "Admin list cannot be null.");
    }

    /**
     * Starts the application console loop displaying the main entry banner and root menu.
     */
    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = E195569ConsoleReader.readInt("Enter choice (" + MAIN_MENU_MIN + "-" + MAIN_MENU_MAX + "): ",
                    MAIN_MENU_MIN, MAIN_MENU_MAX);
            switch (choice) {
                case 1 -> handleAdminFlow();
                case 2 -> handleGuestFlow();
                case 3 -> {
                    System.out.println("\n" + BANNER_BORDER);
                    System.out.println("          Thank you for using SmartStay Hotel Booking System. Goodbye!          ");
                    System.out.println(BANNER_BORDER);
                    running = false;
                }
            }
        }
    }

    private void displayMainMenu() {
        System.out.println(BANNER_BORDER);
        System.out.println("                        SMARTSTAY HOTEL BOOKING SYSTEM                         ");
        System.out.println(BANNER_BORDER);
        System.out.println(" [1] Admin");
        System.out.println(" [2] Guest");
        System.out.println(" [3] Exit");
        System.out.println(DIVIDER);
    }

    // =========================================================================
    // ADMINISTRATOR WORKFLOW
    // =========================================================================

    private void handleAdminFlow() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                              ADMINISTRATOR LOGIN                               ");
        System.out.println(DIVIDER);

        boolean authenticated = false;
        E195569Admin loggedInAdmin = null;

        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            String pin = E195569ConsoleReader.readString("Enter Security PIN (Attempt " + attempt + " of " + MAX_PIN_ATTEMPTS + "): ");
            for (E195569Admin admin : admins) {
                if (admin.login(pin)) {
                    authenticated = true;
                    loggedInAdmin = admin;
                    break;
                }
            }
            if (authenticated) {
                break;
            } else {
                System.out.println("[ERROR] Invalid PIN entered.");
                if (attempt < MAX_PIN_ATTEMPTS) {
                    System.out.println("[ERROR] Remaining attempts: " + (MAX_PIN_ATTEMPTS - attempt));
                }
            }
        }

        if (!authenticated) {
            System.out.println("\n" + DIVIDER);
            System.out.println("[ERROR] Maximum PIN attempts (" + MAX_PIN_ATTEMPTS + ") exceeded. Returning to main menu.");
            System.out.println(DIVIDER);
            pause();
            return;
        }

        System.out.println("\n" + DIVIDER);
        System.out.println("[SUCCESS] Authentication successful. Welcome, " + loggedInAdmin.getFullName() + "!");
        System.out.println(DIVIDER);
        pause();

        boolean inAdmin = true;
        while (inAdmin) {
            displayAdminMenu();
            int choice = E195569ConsoleReader.readInt("Enter selection (" + ADMIN_MENU_MIN + "-" + ADMIN_MENU_MAX + "): ",
                    ADMIN_MENU_MIN, ADMIN_MENU_MAX);
            switch (choice) {
                case 1 -> handleAddRoom();
                case 2 -> handleUpdateRoom();
                case 3 -> handleRemoveRoom();
                case 4 -> handleViewAllRooms();
                case 5 -> handleChangeRoomStatus();
                case 6 -> {
                    System.out.println("\n" + DIVIDER);
                    System.out.println("[SUCCESS] Admin logged out successfully.");
                    System.out.println(DIVIDER);
                    pause();
                    inAdmin = false;
                }
            }
        }
    }

    private void displayAdminMenu() {
        System.out.println("\n" + BANNER_BORDER);
        System.out.println("                               ADMINISTRATOR MENU                               ");
        System.out.println(BANNER_BORDER);
        System.out.println(" [1] Add Room");
        System.out.println(" [2] Update Room");
        System.out.println(" [3] Remove Room");
        System.out.println(" [4] View All Rooms");
        System.out.println(" [5] Change Room Status");
        System.out.println(" [6] Logout");
        System.out.println(DIVIDER);
    }

    private void handleAddRoom() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                                [ ADD NEW ROOM ]                                ");
        System.out.println(DIVIDER);
        String roomId = E195569ConsoleReader.readString("Enter Room ID (e.g., R109): ");
        E195569RoomType roomType = E195569ConsoleReader.readRoomType("Select Room Category:");
        E195569BedType bedType = E195569ConsoleReader.readBedType("Select Bed Type:");
        System.out.println(DIVIDER);
        try {
            E195569Room room = new E195569Room(roomId, roomType, bedType, E195569RoomStatus.AVAILABLE);
            roomService.addRoom(room);
            System.out.println("[SUCCESS] Room " + roomId + " added successfully.");
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleUpdateRoom() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                                [ UPDATE ROOM ]                                 ");
        System.out.println(DIVIDER);
        String roomId = E195569ConsoleReader.readString("Enter Room ID to update: ");
        try {
            roomService.findById(roomId);
            E195569RoomType roomType = E195569ConsoleReader.readRoomType("Select New Room Category:");
            E195569BedType bedType = E195569ConsoleReader.readBedType("Select New Bed Type:");
            E195569RoomStatus status = promptRoomStatus();
            System.out.println(DIVIDER);
            roomService.updateRoom(roomId, roomType, bedType, status);
            System.out.println("[SUCCESS] Room " + roomId + " updated successfully.");
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println(DIVIDER);
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleRemoveRoom() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                                [ REMOVE ROOM ]                                 ");
        System.out.println(DIVIDER);
        String roomId = E195569ConsoleReader.readString("Enter Room ID to remove: ");
        System.out.println(DIVIDER);
        try {
            roomService.removeRoom(roomId);
            System.out.println("[SUCCESS] Room " + roomId + " removed successfully from inventory.");
        } catch (E195569BookingException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleViewAllRooms() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                             [ ALL ROOMS INVENTORY ]                            ");
        System.out.println(DIVIDER);
        List<E195569Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("[ERROR] No rooms registered in the system.");
        } else {
            printRoomsTable(rooms);
            System.out.println("[SUCCESS] Total rooms in catalog: " + rooms.size());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleChangeRoomStatus() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                             [ CHANGE ROOM STATUS ]                             ");
        System.out.println(DIVIDER);
        String roomId = E195569ConsoleReader.readString("Enter Room ID: ");
        try {
            roomService.findById(roomId);
            E195569RoomStatus newStatus = promptRoomStatus();
            System.out.println(DIVIDER);
            roomService.updateStatus(roomId, newStatus);
            System.out.println("[SUCCESS] Room " + roomId + " status updated to " + newStatus.getDisplayName() + ".");
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println(DIVIDER);
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private E195569RoomStatus promptRoomStatus() {
        System.out.println("Select Room Status:");
        E195569RoomStatus[] statuses = E195569RoomStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("  [%d] %s%n", (i + 1), statuses[i].getDisplayName());
        }
        int choice = E195569ConsoleReader.readInt("Select status (1-" + statuses.length + "): ", 1, statuses.length);
        return statuses[choice - 1];
    }

    // =========================================================================
    // GUEST WORKFLOW
    // =========================================================================

    private void handleGuestFlow() {
        boolean inGuest = true;
        while (inGuest) {
            displayGuestMenu();
            int choice = E195569ConsoleReader.readInt("Enter selection (" + GUEST_MENU_MIN + "-" + GUEST_MENU_MAX + "): ",
                    GUEST_MENU_MIN, GUEST_MENU_MAX);
            switch (choice) {
                case 1 -> handleRegisterGuest();
                case 2 -> handleSearchAvailableRooms();
                case 3 -> handleMakeReservation();
                case 4 -> handleSearchReservations();
                case 5 -> handleViewReservationsByCheckInDate();
                case 6 -> handleCancelReservation();
                case 7 -> handleViewInvoiceByBookingId();
                case 8 -> inGuest = false;
            }
        }
    }

    private void displayGuestMenu() {
        System.out.println("\n" + BANNER_BORDER);
        System.out.println("                                   GUEST MENU                                   ");
        System.out.println(BANNER_BORDER);
        System.out.println(" [1] Register Guest");
        System.out.println(" [2] Search Available Rooms by Type and Dates");
        System.out.println(" [3] Make a Reservation");
        System.out.println(" [4] Search Reservations by Guest Name or Booking ID");
        System.out.println(" [5] View Reservations by Check-In Date");
        System.out.println(" [6] Cancel a Reservation");
        System.out.println(" [7] View Invoice by Booking ID");
        System.out.println(" [8] Back to Main Menu");
        System.out.println(DIVIDER);
    }

    private void handleRegisterGuest() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                              [ GUEST REGISTRATION ]                            ");
        System.out.println(DIVIDER);
        String nic = E195569ConsoleReader.readString("Enter NIC or Passport Number: ");
        String name = E195569ConsoleReader.readString("Enter Full Name: ");
        String phone = E195569ConsoleReader.readPhoneNumber("Enter Contact Number: ");
        String email = E195569ConsoleReader.readEmail("Enter Email Address: ");
        System.out.println(DIVIDER);
        try {
            E195569Guest guest = guestService.registerGuest(nic, name, phone, email);
            System.out.println("[SUCCESS] Guest successfully registered: " + guest.getDetails());
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleSearchAvailableRooms() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                    [ SEARCH AVAILABLE ROOMS BY TYPE & DATES ]                  ");
        System.out.println(DIVIDER);
        E195569RoomType roomType = E195569ConsoleReader.readRoomType("Select Room Category:");
        LocalDate checkIn = E195569ConsoleReader.readDate("Enter Check-in Date");
        LocalDate checkOut = E195569ConsoleReader.readDate("Enter Check-out Date");
        System.out.println(DIVIDER);
        try {
            List<E195569Room> available = bookingService.searchAvailableRooms(roomType, checkIn, checkOut);
            if (available.isEmpty()) {
                System.out.println("[ERROR] No available " + roomType.getDisplayName() + " rooms between " + checkIn + " and " + checkOut + ".");
            } else {
                System.out.println("Available " + roomType.getDisplayName() + " Rooms (" + checkIn + " to " + checkOut + "):");
                printRoomsTable(available);
                System.out.println("[SUCCESS] Found " + available.size() + " available room(s).");
            }
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleMakeReservation() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                              [ MAKE RESERVATION ]                              ");
        System.out.println(DIVIDER);
        String nic = E195569ConsoleReader.readString("Enter Guest NIC/Passport: ");
        E195569RoomType roomType = E195569ConsoleReader.readRoomType("Select Room Category:");
        int guests = E195569ConsoleReader.readInt("Enter Total Number of Guests (" + MIN_GUESTS + "-" + MAX_GUESTS + "): ",
                MIN_GUESTS, MAX_GUESTS);
        LocalDate checkIn = E195569ConsoleReader.readDate("Enter Check-in Date");
        LocalDate checkOut = E195569ConsoleReader.readDate("Enter Check-out Date");
        System.out.println(DIVIDER);
        try {
            E195569Booking booking = bookingService.createBooking(nic, roomType, guests, checkIn, checkOut);
            System.out.println("[SUCCESS] Reservation Confirmed Successfully!");
            System.out.println("Booking ID    : " + booking.getBookingId());
            System.out.println("Room Assigned : #" + booking.getRoom().getRoomId() + " (" + booking.getRoom().getRoomType().getDisplayName() + ")");
            System.out.println();
            E195569Invoice invoice = invoiceCalculator.calculate(booking);
            invoice.print();
        } catch (E195569BookingException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleSearchReservations() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                             [ SEARCH RESERVATIONS ]                            ");
        System.out.println(DIVIDER);
        System.out.println("Search By:");
        System.out.println(" [1] Booking ID");
        System.out.println(" [2] Guest Name");
        int choice = E195569ConsoleReader.readInt("Enter choice (" + SEARCH_CHOICE_MIN + "-" + SEARCH_CHOICE_MAX + "): ",
                SEARCH_CHOICE_MIN, SEARCH_CHOICE_MAX);
        System.out.println(DIVIDER);
        if (choice == 1) {
            String id = E195569ConsoleReader.readString("Enter Booking ID: ");
            System.out.println(DIVIDER);
            try {
                E195569Booking booking = bookingService.searchByBookingId(id);
                printBookingsTable(List.of(booking));
                System.out.println("[SUCCESS] Reservation located successfully for ID: " + id);
            } catch (E195569BookingException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        } else {
            String name = E195569ConsoleReader.readString("Enter Guest Name (full or partial): ");
            System.out.println(DIVIDER);
            List<E195569Booking> results = bookingService.searchByGuestName(name);
            if (results.isEmpty()) {
                System.out.println("[ERROR] No reservations found matching guest name: '" + name + "'.");
            } else {
                printBookingsTable(results);
                System.out.println("[SUCCESS] Found " + results.size() + " matching reservation(s).");
            }
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleViewReservationsByCheckInDate() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                   [ RESERVATIONS (SORTED BY CHECK-IN DATE) ]                   ");
        System.out.println(DIVIDER);
        List<E195569Booking> bookings = bookingService.viewByCheckInDate();
        if (bookings.isEmpty()) {
            System.out.println("[ERROR] No reservations found in the system.");
        } else {
            printBookingsTable(bookings);
            System.out.println("[SUCCESS] Total reservations displayed: " + bookings.size());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleCancelReservation() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                             [ CANCEL RESERVATION ]                             ");
        System.out.println(DIVIDER);
        System.out.println("Note: Cancellations within 24 hours of check-in at 00:00 are strictly non-permitted.");
        String bookingId = E195569ConsoleReader.readString("Enter Booking ID to cancel: ");
        System.out.println(DIVIDER);
        try {
            E195569Booking cancelled = bookingService.cancelBooking(bookingId);
            System.out.println("[SUCCESS] Booking " + cancelled.getBookingId() + " has been successfully CANCELLED.");
            System.out.printf("[SUCCESS] Refundable advance of LKR %,.2f released to guest.%n", cancelled.getAdvancePaid());
        } catch (E195569BookingException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    private void handleViewInvoiceByBookingId() {
        System.out.println("\n" + DIVIDER);
        System.out.println("                            [ VIEW BOOKING INVOICE ]                            ");
        System.out.println(DIVIDER);
        String bookingId = E195569ConsoleReader.readString("Enter Booking ID: ");
        System.out.println(DIVIDER);
        try {
            E195569Invoice invoice = bookingService.generateInvoice(bookingId);
            invoice.print();
            System.out.println("[SUCCESS] Invoice displayed successfully for Booking ID: " + bookingId);
        } catch (E195569BookingException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println(DIVIDER);
        pause();
    }

    // =========================================================================
    // PRESENTATION & TABLE FORMATTING HELPERS
    // =========================================================================

    private void printRoomsTable(List<E195569Room> rooms) {
        System.out.println("+----------+----------------+--------------+--------------------+---------------+");
        System.out.println("| Room ID  | Category       | Bed Type     | Current Status     | Daily Rate    |");
        System.out.println("+----------+----------------+--------------+--------------------+---------------+");
        for (E195569Room r : rooms) {
            System.out.println(r);
        }
        System.out.println("+----------+----------------+--------------+--------------------+---------------+");
    }

    private void printBookingsTable(List<E195569Booking> bookings) {
        System.out.println("+-----------+---------+------------------+------------+------------+-------------+-------------+---------------+-----------+");
        System.out.println("| BookingID | Room ID | Guest Name       | Check-In   | Check-Out  | Duration    | Guests      | Advance Paid  | Status    |");
        System.out.println("+-----------+---------+------------------+------------+------------+-------------+-------------+---------------+-----------+");
        for (E195569Booking b : bookings) {
            System.out.println(b);
        }
        System.out.println("+-----------+---------+------------------+------------+------------+-------------+-------------+---------------+-----------+");
    }

    private void pause() {
        E195569ConsoleReader.pause();
    }
}
