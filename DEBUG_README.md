# Gradle Debug Configuration

このプロジェクトでは、Gradleのデバッグ機能を有効にして、テスト実行時の詳細な情報を確認できるように設定されています。

## デバッグ機能の使用方法

### 1. 通常のテスト実行（デバッグ情報付き）
```bash
./gradlew test
```

### 2. 詳細デバッグ情報付きテスト実行
```bash
./gradlew debugTest
```

### 3. 特定のテストクラスのみ実行
```bash
./gradlew test --tests JdbcAccountRepositoryTest
```

### 4. 特定のテストメソッドのみ実行
```bash
./gradlew test --tests JdbcAccountRepositoryTest.testCreateAccount_ShouldCreateNewAccount_WhenValidPersonProvided
```

### 5. デバッグモードでGradle実行
```bash
./gradlew test --debug
```

### 6. 詳細ログ出力でGradle実行
```bash
./gradlew test --info
```

## 有効化されたデバッグ機能

### テスト実行時のデバッグ情報
- テストの開始・終了・成功・失敗・スキップの詳細ログ
- 標準出力と標準エラーの表示
- スタックトレースの詳細表示
- 例外とその原因の詳細表示
- テスト実行の粒度レベル2での表示

### JVMデバッグ
- リモートデバッグポート5005での接続可能
- デバッガーでのブレークポイント設定可能

### システムプロパティ
- `org.gradle.debug=true`: Gradleデバッグモード
- `org.gradle.test.debug=true`: テストデバッグモード
- `org.gradle.logging.level=debug`: ログレベルをDEBUGに設定

## 設定ファイル

### gradle.properties
- `org.gradle.debug=true`: プロジェクト全体のデバッグモード
- `org.gradle.logging.level=debug`: ログレベル設定
- `org.gradle.logging.console=verbose`: コンソール出力の詳細化

### build.gradle
- テストスイートのデバッグ設定
- カスタムデバッグテストタスク
- 詳細なテストログ設定

## トラブルシューティング

### テストが失敗する場合
1. `./gradlew test --info` で詳細情報を確認
2. `./gradlew debugTest` でデバッグ情報付きで実行
3. ログファイルを確認（build/reports/tests/）

### デバッグ接続ができない場合
1. ポート5005が使用可能か確認
2. ファイアウォール設定を確認
3. `./gradlew debugTest --debug` で詳細ログを確認

## 注意事項

- デバッグモードでは実行速度が遅くなる場合があります
- 本番環境ではデバッグ設定を無効にすることを推奨します
- リモートデバッグを使用する場合は、セキュリティ設定に注意してください 