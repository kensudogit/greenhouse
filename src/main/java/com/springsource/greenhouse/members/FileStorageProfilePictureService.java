/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.members;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.FileData;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.utils.ImageUtils;

/**
 * PictureProfileServiceは、プロフィール画像をFileStorageに保存します。
 * プロフィール画像が設定されると、メンバーのアカウントに関連付けられたフラグも設定されます。
 * このフラグは、AccountおよびProfileオブジェクトをマッピングする際にpictureUrlを構築するために使用されます。
 * 
 * @author Keith Donald
 * @author Craig Walls
 */
@Service
public class FileStorageProfilePictureService implements ProfilePictureService {

	private final FileStorage storage;

	private final JdbcTemplate jdbcTemplate;

	private static final String PROFILE_PICS_PATH = "profile-pics/";

	/**
	 * FileStorageProfilePictureServiceを構築します。
	 * 
	 * @param storage      ファイルストレージの実装。例えば、S3やローカルディレクトリにファイルを保存するため。
	 * @param jdbcTemplate pictureSetフラグを更新するために使用されるデータアクセステンプレート。
	 */
	@Inject
	public FileStorageProfilePictureService(FileStorage storage, JdbcTemplate jdbcTemplate) {
		this.storage = storage;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * プロフィール画像を保存します。
	 * 
	 * @param accountId  アカウントID
	 * @param imageBytes 画像のバイト配列
	 * @throws IOException 入出力例外が発生した場合
	 */
	public void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException {
		assertBytesLength(imageBytes);
		String contentType = guessContentType(imageBytes);
		assertContentType(contentType);
		storage.storeFile(new FileData(PROFILE_PICS_PATH + accountId + "/small.jpg",
				ImageUtils.scaleImageToWidth(imageBytes, 50), contentType));
		storage.storeFile(new FileData(PROFILE_PICS_PATH + accountId + "/normal.jpg",
				ImageUtils.scaleImageToWidth(imageBytes, 100), contentType));
		storage.storeFile(new FileData(PROFILE_PICS_PATH + accountId + "/large.jpg",
				ImageUtils.scaleImageToWidth(imageBytes, 200), contentType));
		jdbcTemplate.update("update Member set pictureSet = true where id = ?", accountId);
	}

	// 内部ヘルパーメソッド

	/**
	 * 画像バイト配列の長さを確認します。
	 * 
	 * @param imageBytes 画像のバイト配列
	 * @throws IllegalArgumentException 画像バイト配列が空の場合
	 */
	private void assertBytesLength(byte[] imageBytes) {
		if (imageBytes.length == 0) {
			throw new IllegalArgumentException("Cannot accept empty picture byte[] as a profile picture");
		}
	}

	/**
	 * 画像バイト配列からコンテンツタイプを推測します。
	 * 
	 * @param imageBytes 画像のバイト配列
	 * @return コンテンツタイプ
	 * @throws IOException 入出力例外が発生した場合
	 */
	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
	}

	/**
	 * コンテンツタイプを確認します。
	 * 
	 * @param contentType コンテンツタイプ
	 * @throws IllegalArgumentException 許可されていないコンテンツタイプの場合
	 */
	private void assertContentType(String contentType) {
		if (!IMAGE_TYPES.contains(contentType)) {
			throw new IllegalArgumentException(
					"Cannot accept content type '" + contentType + "' as a profile picture.");
		}
	}

	// 許可されている画像タイプのリスト
	private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");

}