package com.springsource.greenhouse.validation;

// このインターフェースは、フィールド名とその値を検証するための戦略を定義します。
// 各実装は、特定の検証ロジックを提供する必要があります。
@FunctionalInterface
public interface ValidatorStrategy {
    // 指定されたフィールド名と値を検証し、ValidationResultを返します。
    // フィールド名: 検証するフィールドの名前
    // 値: 検証する値
    ValidationResult validate(String fieldName, String value);
}
