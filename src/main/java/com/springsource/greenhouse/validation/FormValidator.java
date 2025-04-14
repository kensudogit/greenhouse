package com.springsource.greenhouse.validation;

import java.util.*;

// FormValidatorクラスは、フォームのフィールドに対するバリデーションを行うためのクラスです。
// 各フィールドに対して複数のバリデーション戦略を適用し、結果を収集します。

public class FormValidator {

    public static class FieldValidation {
        // FieldValidationクラスは、特定のフィールドに対するバリデーションを表現します。
        // フィールド名、値、バリデーション戦略のリストを持ちます。
        private final String fieldName;
        private final String value;
        private final List<ValidatorStrategy> strategies;

        public FieldValidation(String fieldName, String value, List<ValidatorStrategy> strategies) {
            this.fieldName = fieldName;
            this.value = value;
            this.strategies = strategies;
        }

        public List<ValidationResult> validate() {
            // validateメソッドは、各バリデーション戦略を適用し、
            // バリデーション結果をリストとして返します。
            List<ValidationResult> results = new ArrayList<>();
            for (ValidatorStrategy strategy : strategies) {
                ValidationResult result = strategy.validate(fieldName, value);
                if (!result.isValid()) {
                    // バリデーションが失敗した場合、結果をリストに追加します。
                    results.add(result);
                }
            }
            return results;
        }
    }

    private final List<FieldValidation> validations = new ArrayList<>();

    public void addField(String fieldName, String value, ValidatorStrategy... strategies) {
        // addFieldメソッドは、新しいフィールドバリデーションを追加します。
        validations.add(new FieldValidation(fieldName, value, Arrays.asList(strategies)));
    }

    public List<ValidationResult> validateAll() {
        // validateAllメソッドは、すべてのフィールドバリデーションを実行し、
        // すべての結果をリストとして返します。
        List<ValidationResult> allResults = new ArrayList<>();
        for (FieldValidation validation : validations) {
            allResults.addAll(validation.validate());
        }
        return allResults;
    }
}
