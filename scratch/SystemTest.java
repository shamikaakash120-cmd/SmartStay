import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import exception.E195569BookingException;
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

/**
 * Automated test suite verifying E195569BookingService, searchAvailableRooms, and clean architecture.
 */
public class SystemTest {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("       SMARTSTAY VERIFIER: REWRITTEN BOOKING SERVICE & CLEAN ARCHITECTURE      ");
        System.out.println("================================================================================");

        int passed = 0;
        int total = 0;

        E195569RoomService roomService = new E195569RoomService();
        E195569GuestService guestService = new E195569GuestService();
        E195569InvoiceCalculator invoiceCalculator = new E195569InvoiceCalculator();
        E195569BookingService bookingService = new E195569BookingService(roomService, guestService, invoiceCalculator);

        // Pre-populate test rooms and guest
        try {
            roomService.addRoom(new E195569Room("101", E195569RoomType.STANDARD, E195569BedType.DOUBLE, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("102", E195569RoomType.STANDARD, E195569BedType.SINGLE, E195569RoomStatus.AVAILABLE));
            roomService.addRoom(new E195569Room("201", E195569RoomType.DELUXE, E195569BedType.QUEEN, E195569RoomStatus.AVAILABLE));
            guestService.registerGuest("200012345678", "Nimal Silva", "+94771234567", "nimal@example.com");
            guestService.registerGuest("N98765432", "Sarah Jenkins", "+94765432100", "sarah@example.com");
        } catch (Exception e) {
            System.err.println("Setup failed: " + e.getMessage());
        }

        // TEST 1: createBooking finds first available room of requested type and sets status to RESERVED
        total++;
        try {
            LocalDate in1 = LocalDate.now().plusDays(3);
            LocalDate out1 = in1.plusDays(4);
            E195569Booking b1 = bookingService.createBooking("200012345678", E195569RoomType.STANDARD, 2, in1, out1);
            if (b1.getBookingId().equals("BK-0001") && b1.getRoom().getRoomId().equals("101")
                    && b1.getRoom().getStatus() == E195569RoomStatus.RESERVED) {
                System.out.println(" [PASS] Test 1: createBooking auto-assigned first Standard room (101) & generated BK-0001.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 1: Assignment or ID mismatch: " + b1);
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 1: " + e.getMessage());
        }

        // TEST 2: Second booking of same type gets the second available room (102)
        total++;
        try {
            LocalDate in2 = LocalDate.now().plusDays(3);
            LocalDate out2 = in2.plusDays(4);
            E195569Booking b2 = bookingService.createBooking("N98765432", E195569RoomType.STANDARD, 1, in2, out2);
            if (b2.getBookingId().equals("BK-0002") && b2.getRoom().getRoomId().equals("102")) {
                System.out.println(" [PASS] Test 2: Second booking assigned next available Standard room (102) with ID BK-0002.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 2: Assignment mismatch: " + b2);
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 2: " + e.getMessage());
        }

        // TEST 3: Third booking when all Standard rooms are booked throws exception
        total++;
        try {
            LocalDate in3 = LocalDate.now().plusDays(3);
            LocalDate out3 = in3.plusDays(4);
            bookingService.createBooking("200012345678", E195569RoomType.STANDARD, 2, in3, out3);
            System.out.println(" [FAIL] Test 3: Overbooking was not rejected!");
        } catch (E195569BookingException e) {
            System.out.println(" [PASS] Test 3: Properly threw exception when no room is free (" + e.getMessage() + ")");
            passed++;
        }

        // TEST 4: Lead time rule (check-in < 2 days after today) rejected
        total++;
        try {
            LocalDate tooEarly = LocalDate.now().plusDays(1);
            bookingService.createBooking("200012345678", E195569RoomType.DELUXE, 2, tooEarly, tooEarly.plusDays(2));
            System.out.println(" [FAIL] Test 4: Lead time rule (< 2 days) was not rejected!");
        } catch (E195569BookingException e) {
            System.out.println(" [PASS] Test 4: Lead time rule rejected successfully (" + e.getMessage() + ")");
            passed++;
        }

        // TEST 5: searchByBookingId (linear search)
        total++;
        try {
            E195569Booking found = bookingService.searchByBookingId("BK-0001");
            if (found.getGuest().getFullName().equals("Nimal Silva")) {
                System.out.println(" [PASS] Test 5: searchByBookingId linear search located BK-0001 correctly.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 5: Incorrect booking returned.");
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 5: " + e.getMessage());
        }

        // TEST 6: searchByGuestName (case-insensitive partial match)
        total++;
        List<E195569Booking> jenkinsBookings = bookingService.searchByGuestName("JENK");
        if (jenkinsBookings.size() == 1 && jenkinsBookings.get(0).getBookingId().equals("BK-0002")) {
            System.out.println(" [PASS] Test 6: searchByGuestName partial case-insensitive match located Sarah Jenkins.");
            passed++;
        } else {
            System.out.println(" [FAIL] Test 6: searchByGuestName failed: " + jenkinsBookings);
        }

        // TEST 7: viewByCheckInDate (comparator sort)
        total++;
        try {
            // Add a booking with a later check-in
            LocalDate later = LocalDate.now().plusDays(10);
            bookingService.createBooking("200012345678", E195569RoomType.DELUXE, 2, later, later.plusDays(3));
            List<E195569Booking> sorted = bookingService.viewByCheckInDate();
            boolean isSorted = true;
            for (int i = 0; i < sorted.size() - 1; i++) {
                if (sorted.get(i).getCheckInDate().isAfter(sorted.get(i + 1).getCheckInDate())) {
                    isSorted = false;
                    break;
                }
            }
            if (isSorted && sorted.size() == 3) {
                System.out.println(" [PASS] Test 7: viewByCheckInDate properly sorted bookings chronologically.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 7: viewByCheckInDate sorting incorrect.");
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 7: " + e.getMessage());
        }

        // TEST 8: cancelBooking (cancels booking & sets room status back to AVAILABLE)
        total++;
        try {
            E195569Booking cancelled = bookingService.cancelBooking("BK-0001");
            E195569Room room101 = roomService.findById("101");
            if (cancelled.getBookingStatus().name().equals("CANCELLED") && room101.isAvailable()) {
                System.out.println(" [PASS] Test 8: cancelBooking set status to CANCELLED and room 101 back to AVAILABLE.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 8: Status not properly updated upon cancellation.");
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 8: " + e.getMessage());
        }

        // TEST 9: generateInvoice
        total++;
        try {
            E195569Invoice inv = bookingService.generateInvoice("BK-0002");
            if (inv != null && inv.getFinalAmount() > 0) {
                System.out.println(" [PASS] Test 9: generateInvoice successfully returned invoice for BK-0002.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 9: Invoice generation failed.");
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 9: " + e.getMessage());
        }

        // TEST 10: searchAvailableRooms (validates check-out is after check-in and reuses overlap check)
        total++;
        try {
            LocalDate validIn = LocalDate.now().plusDays(20);
            LocalDate validOut = validIn.plusDays(3);
            LocalDate invalidOut = validIn.minusDays(1);

            // 1. Invalid date check: check-out before check-in must throw E195569BookingException
            boolean dateValidationCaught = false;
            try {
                bookingService.searchAvailableRooms(E195569RoomType.STANDARD, validIn, invalidOut);
            } catch (E195569BookingException e) {
                dateValidationCaught = true;
            }

            // 2. Query available Standard rooms for dates (Room 101 is AVAILABLE after cancellation)
            List<E195569Room> nonOverlapping = bookingService.searchAvailableRooms(E195569RoomType.STANDARD, validIn, validOut);

            if (dateValidationCaught && nonOverlapping.size() == 1 && nonOverlapping.get(0).getRoomId().equals("101")) {
                System.out.println(" [PASS] Test 10: searchAvailableRooms() validated check-out > check-in & correctly returned available Room 101.");
                passed++;
            } else {
                System.out.println(" [FAIL] Test 10: searchAvailableRooms() failed check. DateCaught=" + dateValidationCaught + ", count=" + nonOverlapping.size());
            }
        } catch (Exception e) {
            System.out.println(" [FAIL] Test 10: " + e.getMessage());
        }

        System.out.println("================================================================================");
        System.out.printf(" RESULTS: %d / %d Tests Passed (%.1f%%)%n", passed, total, (passed * 100.0 / total));
        System.out.println("================================================================================");
    }
}
