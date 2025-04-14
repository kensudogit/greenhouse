package com.springsource.greenhouse.config;

import java.util.*;

public class FormValidator {

    public static class FieldValidation {
        private final String fieldName;
        private final String value;
        private final List<ValidatorStrategy> strategies;

        public FieldValidation(String fieldName, String value, List<ValidatorStrategy> strategies) {
            this.fieldName = fieldName;
            this.value = value;
            this.strategies = strategies;
        }

        public List<ValidationResult> validate() {
            List<ValidationResult> results = new ArrayList<>();
            for (ValidatorStrategy strategy : strategies) {
                ValidationResult result = strategy.validate(fieldName, value);
                if (!result.isValid()) {
                    results.add(result);
                }
            }
            return results;
        }
    }

    private final List<FieldValidation> validations = new ArrayList<>();

    public void addField(String fieldName, String value, ValidatorStrategy... strategies) {
        validations.add(new FieldValidation(fieldName, value, Arrays.asList(strategies)));
    }

    public List<ValidationResult> validateAll() {
        List<ValidationResult> allResults = new ArrayList<>();
        for (FieldValidation validation : validations) {
            allResults.addAll(validation.validate());
        }
        return allResults;
    }
}
