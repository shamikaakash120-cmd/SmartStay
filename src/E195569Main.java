import java.util.ArrayList;
import java.util.List;
import model.E195569Admin;
import service.E195569BookingService;
import service.E195569GuestService;
import service.E195569InvoiceCalculator;
import service.E195569RoomService;
import ui.E195569ConsoleMenu;
import util.E195569DataSeeder;

/**
 * Main application entry point for the SmartStay Hotel Booking System.
 * Solely responsible for initializing service instances, seeding baseline demonstration data,
 * and transferring control to the console user interface.
 */
public class E195569Main {

    /**
     * Application execution entry point.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        // 1. Initialize Core Business Services
        E195569RoomService roomService = new E195569RoomService();
        E195569GuestService guestService = new E195569GuestService();
        E195569InvoiceCalculator invoiceCalculator = new E195569InvoiceCalculator();
        E195569BookingService bookingService = new E195569BookingService(roomService, guestService, invoiceCalculator);

        // Administrator credentials store
        List<E195569Admin> admins = new ArrayList<>();

        // 2. Populate Baseline Seed Data
        E195569DataSeeder.seed(roomService, guestService, admins);

        // 3. Launch Console User Interface
        E195569ConsoleMenu consoleMenu = new E195569ConsoleMenu(
                roomService, guestService, bookingService, invoiceCalculator, admins
        );
        consoleMenu.start();
    }
}
