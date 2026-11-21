package exception;

/**
 * Checked exception thrown when a hotel booking or service operation violates business rules.
 */
public class E195569BookingException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an E195569BookingException with a detailed error message.
     *
     * @param message Description of the rule violation.
     */
    public E195569BookingException(String message) {
        super(message);
    }
}
