package com.springsource.greenhouse.validation;

import java.util.regex.Pattern;

public class CommonValidator {

    // Private constructor to prevent instantiation
    private CommonValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // 必須チェック（nullや空文字列を許容しない）
    public static boolean isRequired(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // 文字列長チェック（最小・最大）
    public static boolean isLengthBetween(String value, int min, int max) {
        if (value == null)
            return false;
        int length = value.length();
        return length >= min && length <= max;
    }

    // 数字のみかどうか
    public static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }

    // メールアドレス形式チェック
    public static boolean isValidEmail(String value) {
        if (value == null)
            return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, value);
    }

    // 電話番号形式チェック（ハイフンあり/なしを許容）
    public static boolean isValidPhoneNumber(String value) {
        if (value == null)
            return false;
        String phoneRegex = "^(\\d{2,4}-\\d{2,4}-\\d{3,4}|\\d{10,11})$";
        return Pattern.matches(phoneRegex, value);
    }

    // アルファベットのみかどうか
    public static boolean isAlphabetic(String value) {
        return value != null && value.matches("^[A-Za-z]+$");
    }

    // アルファベットと数字のみかどうか
    public static boolean isAlphanumeric(String value) {
        return value != null && value.matches("^[A-Za-z0-9]+$");
    }

    // URL形式チェック
    public static boolean isValidURL(String value) {
        if (value == null)
            return false;
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return Pattern.matches(urlRegex, value);
    }

    // 汎用正規表現チェックメソッド
    // 使用例:
    // boolean isMatch = CommonValidator.isValidByRegex("example@example.com",
    // "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    // System.out.println("Is valid email: " + isMatch);
    public static boolean isValidByRegex(String value, String regex) {
        if (value == null || regex == null)
            return false;
        return Pattern.matches(regex, value);
    }

}
