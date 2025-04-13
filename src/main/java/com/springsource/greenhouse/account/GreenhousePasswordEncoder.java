/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * SHA-256ハッシュを使用したPasswordEncoderの実装。
 * Sunプロバイダーによる8バイトのランダムソルトをデフォルトで使用します。
 * 互換性の注意: この実装はダイジェストの繰り返しを行いません。
 * これは既存のパスワードデータベースとの互換性のためです。
 * より安全なダイジェストを行う実装については、StandardPasswordEncoderを参照してください。
 * 
 * @author Keith Donald
 * @see StandardPasswordEncoder
 */
public class GreenhousePasswordEncoder implements PasswordEncoder {

	private final Digester digester;

	private final byte[] secret;

	private final BytesKeyGenerator saltGenerator;

	/**
	 * 標準のパスワードエンコーダーを構築します。
	 * 
	 * @param secret エンコーディングプロセスで使用される秘密鍵
	 */
	public GreenhousePasswordEncoder(String secret) {
		this("SHA-256", "SUN", secret);
	}

	/**
	 * 完全にカスタマイズされた標準のパスワードエンコーダーを作成します。
	 */
	public GreenhousePasswordEncoder(String algorithm, String provider, String secret) {
		this.digester = new Digester(algorithm, provider);
		this.secret = Utf8.encode(secret);
		this.saltGenerator = KeyGenerators.secureRandom();
	}

	/**
	 * パスワードをエンコードします。
	 */
	public String encode(CharSequence rawPassword) {
		return encode(rawPassword, saltGenerator.generateKey());
	}

	/**
	 * パスワードが一致するかどうかを確認します。
	 */
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		byte[] digested = decode(encodedPassword);
		byte[] salt = subArray(digested, 0, saltGenerator.getKeyLength());
		return matches(digested, digest(rawPassword, salt));
	}

	// internal helpers

	/**
	 * パスワードをエンコードします。
	 */
	private String encode(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digest(rawPassword, salt);
		return new String(Hex.encode(digest));
	}

	/**
	 * パスワードをダイジェストします。
	 */
	private byte[] digest(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digester.digest(concatenate(salt, secret, Utf8.encode(rawPassword)));
		return concatenate(salt, digest);
	}

	/**
	 * エンコードされたパスワードをデコードします。
	 */
	private byte[] decode(String encodedPassword) {
		return Hex.decode(encodedPassword);
	}

	/**
	 * 期待されるバイト配列と実際のバイト配列が一致するかどうかを確認します。
	 */
	private boolean matches(byte[] expected, byte[] actual) {
		return Arrays.equals(expected, actual);
	}

	private static class Digester {

		private final MessageDigest messageDigest;

		/**
		 * ダイジェストを計算します。
		 * 追加のセキュリティのために、少なくとも1024回の繰り返しを適用する必要があります。
		 * 残念ながら、パスワードデータベースが作成されたときにはこれが行われませんでした。
		 * したがって、互換性のあるダイジェスト動作を維持する必要があります。
		 */
		public Digester(String algorithm, String provider) {
			try {
				messageDigest = MessageDigest.getInstance(algorithm, provider);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("No such hashing algorithm", e);
			} catch (NoSuchProviderException e) {
				throw new IllegalStateException("No such provider for hashing algorithm", e);
			}
		}

		public byte[] digest(byte[] value) {
			// at least 1024 iterations should be applied here for additional security
			// against brute-force attacks.
			// Unfortunately this was not done when the password database was populated.
			// Thus, we need to preserve compatible digest behavior.
			synchronized (messageDigest) {
				return messageDigest.digest(value);
			}
		}

	}

}
