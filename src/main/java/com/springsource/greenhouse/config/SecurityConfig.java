/*
 * 著作権 2010-2011 オリジナルの著者または著者。
 *
 * Apache License, Version 2.0 ("ライセンス") に基づいてライセンスされています。
 * ライセンスに従わない限り、このファイルを使用することはできません。
 * ライセンスのコピーは以下で入手できます。
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * 適用される法律または書面によって同意されている場合を除き、
 * ソフトウェアは「現状のまま」配布され、
 * 明示的または暗示的な保証はありません。
 * ライセンスに基づく権限と制限を参照してください。
 */
package com.springsource.greenhouse.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springsource.greenhouse.account.GreenhousePasswordEncoder;

/**
 * Spring Security の設定。
 * Greenhouse ウェブアプリケーションを保護するポリシーを適用します。
 * <p>
 * 組み込みモードでは、開発者の利便性とテストの容易さのために、
 * パスワードエンコーディングやデータ暗号化を行いません。
 * また、一時的な OAuth セッションをメモリ内の ConcurrentHashMap に保存します。
 * </p>
 * <p>
 * 標準モードでは、標準のパスワードエンコーディング（SHA-256 1024 回の反復ハッシュ化 + ランダムソルト）
 * および暗号化（パスワードベースの 256 ビット AES + サイト全体のソルト + セキュアランダム 16 バイト iV 処理）を適用します。
 * 一時的な OAuth セッションを Redis キー値ストアに保存します。
 * </p>
 * <p>
 * Spring Security は現在、XML 名前空間を使用して最もよく設定されているため、
 * このクラスはほとんどの設定情報を含む XML ファイルをインポートします。
 * PasswordEncoder、TextEncryptor、および OAuthSessionManager は Java で設定されています。
 * </p>
 * 
 * @author Keith Donald
 */
@Configuration
@ImportResource("classpath:com/springsource/greenhouse/config/security.xml")
public class SecurityConfig {

	// Private constructor to prevent instantiation
	private SecurityConfig() {
		// This constructor is intentionally empty. Nothing special is needed here.
	}

	/**
	 * 組み込みセキュリティ設定（安全ではありません）。
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.noOpText();
		}

	}

	/**
	 * 標準のセキュリティ設定（安全）。
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		private final Environment environment;

		public Standard(Environment environment) {
			this.environment = environment;
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new GreenhousePasswordEncoder(getEncryptPassword());
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.queryableText(getEncryptPassword(), environment.getProperty("security.encryptSalt"));
		}

		// helpers

		private String getEncryptPassword() {
			return environment.getProperty("security.encryptPassword");
		}

	}

}