import java.time.LocalDate;
import model.E195569BedType;
import model.E195569Booking;
import model.E195569Guest;
import model.E195569Invoice;
import model.E195569Room;
import model.E195569RoomStatus;
import model.E195569RoomType;
import service.E195569InvoiceCalculator;

/**
 * Temporary scratch verification script to validate invoice calculation on Deluxe, 5-night, 4-guest booking.
 */
public class InvoiceCheck {
    public static void main(String[] args) {
        // 1. Setup Guest
        E195569Guest guest = new E195569Guest("200012345678", "Nimal Silva", "+94771234567", "nimal.silva@example.com");

        // 2. Setup Deluxe Room (Daily Rate: 9,000, Capacity: 3, Extra: 1,500, Tax: 12%, Breakfast: Yes)
        E195569Room room = new E195569Room("D201", E195569RoomType.DELUXE, E195569BedType.QUEEN, E195569RoomStatus.AVAILABLE);

        // 3. Setup 5-night, 4-guest booking
        LocalDate checkIn = LocalDate.of(2026, 12, 1);
        LocalDate checkOut = checkIn.plusDays(5); // 5 nights
        int numberOfGuests = 4; // 1 extra guest above capacity of 3

        E195569Booking booking = new E195569Booking("BK-9001", guest, room, checkIn, checkOut, numberOfGuests);

        // 4. Calculate invoice using E195569InvoiceCalculator
        E195569InvoiceCalculator calculator = new E195569InvoiceCalculator();
        E195569Invoice invoice = calculator.calculate(booking);

        // 5. Print invoice
        invoice.print();
    }
}
