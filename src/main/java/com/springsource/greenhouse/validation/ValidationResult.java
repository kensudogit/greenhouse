package com.springsource.greenhouse.validation;

// ValidationResultクラスは、バリデーションの結果を表します。
// validフィールドはバリデーションが成功したかどうかを示し、messageフィールドはエラーメッセージを保持します。

public class ValidationResult {
    public class ValidationResult {
        private final boolean valid;
        private final String message;

        // コンストラクタは、バリデーションの結果を初期化します。
        // valid: バリデーションが成功したかどうかを示すブール値
        // message: エラーメッセージを保持する文字列
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        // バリデーションが成功したかどうかを返します。
        public boolean isValid() {
            return valid;
        }

        // エラーメッセージを返します。
        public String getMessage() {
            return message;
        }

        // バリデーションが成功した結果を返します。
        public static ValidationResult success() {
            return new ValidationResult(true, "");
        }

        // 指定されたメッセージを持つ失敗したバリデーション結果を返します。
        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
    }

}
