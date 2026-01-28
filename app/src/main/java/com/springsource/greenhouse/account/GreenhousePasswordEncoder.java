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
 * PasswordEncoder implementation using SHA-256 hash.
 * Uses 8-byte random salt from Sun provider by default.
 * Compatibility note: This implementation does not perform digest iteration.
 * This is for compatibility with existing password databases.
 * For a more secure digest implementation, see StandardPasswordEncoder.
 * 
 * @author Keith Donald
 * @see StandardPasswordEncoder
 */
public class GreenhousePasswordEncoder implements PasswordEncoder {

	private final Digester digester;

	private final byte[] secret;

	private final BytesKeyGenerator saltGenerator;

	/**
	 * Constructs a standard password encoder.
	 * 
	 * @param secret secret key used in the encoding process
	 */
	public GreenhousePasswordEncoder(String secret) {
		this("SHA-256", "SUN", secret);
	}

	/**
	 * Creates a fully customized standard password encoder.
	 * 
	 * @param algorithm hashing algorithm
	 * @param provider  security provider
	 * @param secret    secret key used in the encoding process
	 */
	public GreenhousePasswordEncoder(String algorithm, String provider, String secret) {
		this.digester = new Digester(algorithm, provider);
		this.secret = Utf8.encode(secret);
		this.saltGenerator = KeyGenerators.secureRandom();
	}

	/**
	 * Encodes a password.
	 * 
	 * @param rawPassword raw password to encode
	 * @return encoded password
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		return encode(rawPassword, saltGenerator.generateKey());
	}

	/**
	 * Checks if a password matches the encoded password.
	 * 
	 * @param rawPassword     raw password to check
	 * @param encodedPassword encoded password to compare against
	 * @return true if passwords match
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		byte[] digested = decode(encodedPassword);
		byte[] salt = subArray(digested, 0, saltGenerator.getKeyLength());
		return matches(digested, digest(rawPassword, salt));
	}

	// internal helpers

	/**
	 * Encodes a password with a specific salt.
	 * 
	 * @param rawPassword raw password to encode
	 * @param salt        salt bytes
	 * @return encoded password string
	 */
	private String encode(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digest(rawPassword, salt);
		return new String(Hex.encode(digest));
	}

	/**
	 * Digests a password with salt.
	 * 
	 * @param rawPassword raw password
	 * @param salt        salt bytes
	 * @return digested bytes
	 */
	private byte[] digest(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digester.digest(concatenate(salt, secret, Utf8.encode(rawPassword)));
		return concatenate(salt, digest);
	}

	/**
	 * Decodes an encoded password.
	 * 
	 * @param encodedPassword encoded password string
	 * @return decoded bytes
	 */
	private byte[] decode(String encodedPassword) {
		return Hex.decode(encodedPassword);
	}

	/**
	 * Checks if expected and actual byte arrays match.
	 * 
	 * @param expected expected byte array
	 * @param actual   actual byte array
	 * @return true if arrays match
	 */
	private boolean matches(byte[] expected, byte[] actual) {
		return Arrays.equals(expected, actual);
	}

	private static class Digester {

		private final MessageDigest messageDigest;

		/**
		 * Computes digest.
		 * For additional security, at least 1024 iterations should be applied.
		 * Unfortunately, this was not done when the password database was created.
		 * Therefore, we need to maintain compatible digest behavior.
		 * 
		 * @param algorithm hashing algorithm
		 * @param provider  security provider
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

		/**
		 * Computes digest for the given value.
		 * Note: At least 1024 iterations should be applied here for additional security
		 * against brute-force attacks. Unfortunately this was not done when the password
		 * database was populated. Thus, we need to preserve compatible digest behavior.
		 * 
		 * @param value value to digest
		 * @return digested bytes
		 */
		public byte[] digest(byte[] value) {
			synchronized (messageDigest) {
				return messageDigest.digest(value);
			}
		}

	}

}
