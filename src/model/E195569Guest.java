package model;

import java.util.Objects;

/**
 * Represents a registered hotel guest in the SmartStay system.
 */
// Inheritance: E195569Guest extends E195569Person, inheriting core contact and identity attributes
public class E195569Guest extends E195569Person {
    private final String nicOrPassport;

    /**
     * Constructs a validated guest entity.
     *
     * @param nicOrPassport National Identity Card (NIC) or Passport number.
     * @param fullName      Full legal name.
     * @param contactNumber Contact telephone number.
     * @param email         Contact email address.
     * @throws IllegalArgumentException if any argument is null or blank.
     */
    public E195569Guest(String nicOrPassport, String fullName, String contactNumber, String email) {
        super(fullName, contactNumber, email);
        if (nicOrPassport == null || nicOrPassport.isBlank()) {
            throw new IllegalArgumentException("NIC or Passport number cannot be null or blank.");
        }
        this.nicOrPassport = nicOrPassport.trim();
    }

    public String getNicOrPassport() {
        return nicOrPassport;
    }

    // Polymorphism: Dynamic method dispatch overrides abstract getDetails() from E195569Person
    @Override
    public String getDetails() {
        return String.format("Guest [NIC/Passport: %s, Name: %s, Contact: %s, Email: %s]",
                nicOrPassport, getFullName(), getContactNumber(), getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        E195569Guest guest = (E195569Guest) o;
        return Objects.equals(nicOrPassport, guest.nicOrPassport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nicOrPassport);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
