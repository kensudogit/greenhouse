package com.springsource.greenhouse.config;

public class ValidationResult {
    public class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, "");
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
    }

}
