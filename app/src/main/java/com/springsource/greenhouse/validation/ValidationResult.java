package com.springsource.greenhouse.validation;

/**
 * ValidationResult class represents the result of validation.
 * The valid field indicates whether validation succeeded, and the message field holds error messages.
 */
public class ValidationResult {
    private final boolean valid;
    private final String message;

    /**
     * Constructs a ValidationResult.
     * 
     * @param valid   boolean indicating whether validation succeeded
     * @param message string holding error message
     */
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Returns whether validation succeeded.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a successful validation result.
     */
    public static ValidationResult success() {
        return new ValidationResult(true, "");
    }

    /**
     * Returns a failed validation result with the specified message.
     */
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}
