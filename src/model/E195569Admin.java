package model;

import java.util.Objects;

/**
 * Represents a hotel administrator account in the SmartStay system.
 * Contains authentication credentials and administrative identity details.
 */
// Inheritance: E195569Admin extends E195569Person to reuse personal contact and name attributes
public class E195569Admin extends E195569Person {
    private final String adminId;
    private final String pin;

    /**
     * Constructs a validated admin account.
     *
     * @param adminId       Unique identifier for the administrator.
     * @param pin           Security PIN for login authentication.
     * @param fullName      Administrator's full name.
     * @param contactNumber Contact telephone number.
     * @param email         Contact email address.
     * @throws IllegalArgumentException if any argument is null or blank.
     */
    public E195569Admin(String adminId, String pin, String fullName, String contactNumber, String email) {
        super(fullName, contactNumber, email);
        if (adminId == null || adminId.isBlank()) {
            throw new IllegalArgumentException("Admin ID cannot be null or blank.");
        }
        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException("PIN cannot be null or blank.");
        }
        this.adminId = adminId.trim();
        this.pin = pin.trim();
    }

    public String getAdminId() {
        return adminId;
    }

    public String getPin() {
        return pin;
    }

    /**
     * Authenticates the administrator by verifying the provided PIN.
     *
     * @param inputPin PIN to test against stored credential.
     * @return true if PIN matches exactly, false otherwise.
     */
    public boolean login(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // Polymorphism: Dynamic method dispatch overrides abstract getDetails() from E195569Person
    @Override
    public String getDetails() {
        return String.format("Admin [ID: %s, Name: %s, Contact: %s, Email: %s]",
                adminId, getFullName(), getContactNumber(), getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        E195569Admin admin = (E195569Admin) o;
        return Objects.equals(adminId, admin.adminId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
