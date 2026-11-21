package service;

import exception.E195569BookingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import model.E195569Guest;

/**
 * Service managing customer and guest registrations with in-memory HashMap storage.
 */
public class E195569GuestService {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{9,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Data Structure: HashMap for O(1) average-time guest retrieval keyed by NIC/Passport
    private final Map<String, E195569Guest> guests = new HashMap<>();

    /**
     * Registers a new guest after thoroughly validating personal details and checking duplicates.
     *
     * @param nicOrPassport National Identity Card (NIC) or Passport number.
     * @param fullName      Full legal name containing letters and spaces only.
     * @param contactNumber Contact phone number (9 to 15 digits with optional leading +).
     * @param email         Valid email address matching standard format.
     * @return Newly registered and stored E195569Guest instance.
     * @throws E195569BookingException if any field is blank, malformed, or already registered.
     */
    public E195569Guest registerGuest(String nicOrPassport, String fullName, String contactNumber, String email)
            throws E195569BookingException {
        if (nicOrPassport == null || nicOrPassport.trim().isBlank()) {
            throw new E195569BookingException("NIC/Passport number cannot be blank.");
        }
        if (fullName == null || fullName.trim().isBlank()) {
            throw new E195569BookingException("Full name cannot be blank.");
        }
        if (contactNumber == null || contactNumber.trim().isBlank()) {
            throw new E195569BookingException("Contact number cannot be blank.");
        }
        if (email == null || email.trim().isBlank()) {
            throw new E195569BookingException("Email address cannot be blank.");
        }

        String trimmedNic = nicOrPassport.trim();
        String trimmedName = fullName.trim();
        String trimmedPhone = contactNumber.trim();
        String trimmedEmail = email.trim();

        if (!NAME_PATTERN.matcher(trimmedName).matches()) {
            throw new E195569BookingException("Invalid name '" + trimmedName + "': Name must contain only letters and spaces.");
        }
        if (!PHONE_PATTERN.matcher(trimmedPhone).matches()) {
            throw new E195569BookingException("Invalid contact number '" + trimmedPhone + "': Must contain 9 to 15 digits with an optional leading '+'.");
        }
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new E195569BookingException("Invalid email address '" + trimmedEmail + "': Does not match standard email pattern.");
        }
        if (guests.containsKey(trimmedNic)) {
            throw new E195569BookingException("Registration failed: Guest with NIC/Passport '" + trimmedNic + "' is already registered.");
        }

        E195569Guest guest = new E195569Guest(trimmedNic, trimmedName, trimmedPhone, trimmedEmail);
        guests.put(trimmedNic, guest);
        return guest;
    }

    /**
     * Overloaded convenience method for registering a guest instance.
     */
    public void registerGuest(E195569Guest guest) throws E195569BookingException {
        if (guest == null) {
            throw new E195569BookingException("Guest reference cannot be null.");
        }
        registerGuest(guest.getNicOrPassport(), guest.getFullName(), guest.getContactNumber(), guest.getEmail());
    }

    /**
     * Finds a registered guest by NIC or Passport number.
     *
     * @param nicOrPassport National identification string.
     * @return Found E195569Guest instance.
     * @throws E195569BookingException if guest does not exist.
     */
    public E195569Guest findByNic(String nicOrPassport) throws E195569BookingException {
        if (nicOrPassport == null || !guests.containsKey(nicOrPassport.trim())) {
            throw new E195569BookingException("Guest with NIC/Passport '" + nicOrPassport + "' was not found.");
        }
        return guests.get(nicOrPassport.trim());
    }

    /**
     * Returns an unmodifiable list of all registered guests.
     */
    public List<E195569Guest> getAllGuests() {
        return Collections.unmodifiableList(new ArrayList<>(guests.values()));
    }
}
