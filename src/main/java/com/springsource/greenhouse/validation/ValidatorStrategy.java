@FunctionalInterface
public interface ValidatorStrategy {
    ValidationResult validate(String fieldName, String value);
}
