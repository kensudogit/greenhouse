package com.springsource.greenhouse.validation;

import java.util.regex.Pattern;

public class CommonValidatorExt {

    // このクラスは、共通のバリデーションロジックを提供するユーティリティクラスです。
    // インスタンス化を防ぐためにプライベートコンストラクタを持ちます。
    private CommonValidatorExt() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ValidationResult isRequired(String value, String fieldName) {
        // このメソッドは、指定されたフィールドが必須であることを確認します。
        // 値がnullまたは空白の場合、失敗の結果を返します。
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + " は必須項目です。");
        }
        return ValidationResult.success();
    }

    public static ValidationResult isLengthBetween(String value, int min, int max, String fieldName) {
        // このメソッドは、指定されたフィールドの長さが指定された範囲内にあるかを確認します。
        // 値がnullの場合、失敗の結果を返します。
        if (value == null) {
            return ValidationResult.failure(fieldName + " の値が不正です。");
        }
        int length = value.length();
        // 長さが範囲外の場合、失敗の結果を返します。
        if (length < min || length > max) {
            return ValidationResult.failure(fieldName + " は " + min + " 文字以上 " + max + " 文字以下で入力してください。");
        }
        return ValidationResult.success();
    }

    public static ValidationResult isNumeric(String value, String fieldName) {
        // このメソッドは、指定されたフィールドが数値であるかを確認します。
        // 値がnullまたは数値でない場合、失敗の結果を返します。
        if (value == null || !value.matches("\\d+")) {
            return ValidationResult.failure(fieldName + " は半角数字で入力してください。");
        }
        return ValidationResult.success();
    }

    public static ValidationResult isValidEmail(String value, String fieldName) {
        // このメソッドは、指定されたフィールドが有効なメールアドレス形式であるかを確認します。
        // 値がnullの場合、失敗の結果を返します。
        if (value == null) {
            return ValidationResult.failure(fieldName + " の値が不正です。");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // メールアドレス形式が不正な場合、失敗の結果を返します。
        if (!Pattern.matches(emailRegex, value)) {
            return ValidationResult.failure(fieldName + " は正しいメールアドレス形式で入力してください。");
        }
        return ValidationResult.success();
    }

    public static ValidationResult isValidPhoneNumber(String value, String fieldName) {
        // このメソッドは、指定されたフィールドが有効な電話番号形式であるかを確認します。
        // 値がnullの場合、失敗の結果を返します。
        if (value == null) {
            return ValidationResult.failure(fieldName + " の値が不正です。");
        }
        String phoneRegex = "^(\\d{2,4}-\\d{2,4}-\\d{3,4}|\\d{10,11})$";
        // 電話番号形式が不正な場合、失敗の結果を返します。
        if (!Pattern.matches(phoneRegex, value)) {
            return ValidationResult.failure(fieldName + " は正しい電話番号形式で入力してください（例：090-1234-5678）。");
        }
        return ValidationResult.success();
    }

    public static ValidationResult matchesRegex(String value, String regex, String fieldName, String errorMessage) {
        // このメソッドは、指定されたフィールドが指定された正規表現に一致するかを確認します。
        // 値または正規表現がnull、または一致しない場合、失敗の結果を返します。
        if (value == null || regex == null || !Pattern.matches(regex, value)) {
            return ValidationResult.failure(fieldName + " " + errorMessage);
        }
        return ValidationResult.success();
    }
}
