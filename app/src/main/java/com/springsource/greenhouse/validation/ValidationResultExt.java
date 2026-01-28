package com.springsource.greenhouse.validation;

// ValidationResultExt クラスは、バリデーションの結果を表すクラスです。
public class ValidationResultExt {
    private final boolean valid;
    private final String message;

    public ValidationResultExt(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    // isValid メソッドは、バリデーションが成功したかどうかを返します。
    public boolean isValid() {
        return valid;
    }

    // getMessage メソッドは、バリデーションの結果に関連するメッセージを返します。
    public String getMessage() {
        return message;
    }

    // success メソッドは、成功したバリデーション結果を生成します。
    public static ValidationResult success() {
        return new ValidationResult(true, "");
    }

    // failure メソッドは、失敗したバリデーション結果を生成し、メッセージを設定します。
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}
