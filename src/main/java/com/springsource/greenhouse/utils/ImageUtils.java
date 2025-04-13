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
package com.springsource.greenhouse.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 画像処理ユーティリティクラス。
 * 
 * @author Craig Walls
 */
public class ImageUtils {

	private ImageUtils() {
		// Prevent instantiation
	}

	/**
	 * バイト配列に格納された画像を特定の幅にスケーリングします。
	 */
	public static byte[] scaleImageToWidth(byte[] originalBytes, int scaledWidth) throws IOException {
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));
		int originalWidth = originalImage.getWidth();
		// 画像の元の幅がスケーリング後の幅以下の場合、元のバイト配列を返します。
		if (originalWidth <= scaledWidth) {
			return originalBytes;
		}
		// スケーリング後の高さを計算します。
		int scaledHeight = (int) (originalImage.getHeight() * ((float) scaledWidth / (float) originalWidth));
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledImage.createGraphics();
		// 画像をスケーリングして描画します。
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ImageIO.write(scaledImage, "jpeg", byteStream);
		return byteStream.toByteArray();
	}

}
