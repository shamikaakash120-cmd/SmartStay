package model;

/**
 * Abstract base class representing a person in the SmartStay system.
 * Encapsulates common identity and contact details shared across system roles.
 */
public abstract class E195569Person {
    private final String fullName;
    private final String contactNumber;
    private final String email;

    /**
     * Constructs a validated Person instance.
     *
     * @param fullName      Full legal name of the person.
     * @param contactNumber Contact telephone number.
     * @param email         Contact email address.
     * @throws IllegalArgumentException if any argument is null or blank.
     */
    public E195569Person(String fullName, String contactNumber, String email) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank.");
        }
        if (contactNumber == null || contactNumber.isBlank()) {
            throw new IllegalArgumentException("Contact number cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }

        this.fullName = fullName.trim();
        this.contactNumber = contactNumber.trim();
        this.email = email.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Abstract method to obtain formatted details specific to the concrete role.
     *
     * @return Formatted detail string.
     */
    public abstract String getDetails();
}
