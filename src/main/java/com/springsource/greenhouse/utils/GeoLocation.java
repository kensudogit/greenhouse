/*
 * Copyright 2012 the original author or authors.
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

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// GeoLocationクラスは、指定された住所の緯度と経度を取得するためのユーティリティクラスです。
// Google Maps APIを使用して、住所を地理座標に変換します。
public class GeoLocation {

	private static final String ENCODING = "UTF-8";
	// This is a Google API Key, generated by a member of the UA Spring team, can be
	// changed if needed
	private static final String KEY = "ABQIAAAAtibbxuabc4IbMMd3Lz6YKxT8h4OmK3OACxx2GTyIwybqxjarwRRNysxHsfcnzRtf9nk6CkJc3fb05Q";
	private static final String CSV_PREFIX = "200,6,";

	private String lon;
	private String lat;

	// GeoLocationのコンストラクタは、緯度と経度を受け取り、インスタンスを初期化します。
	private GeoLocation(String lat, String lon) {
		this.lon = lon;
		this.lat = lat;
	}

	// GeoLocationのインスタンスを文字列として返します。
	public String toString() {
		return "Lat: " + lat + ", Lon: " + lon;
	}

	// 経度をDouble型で返します。
	public Double toLongitude() {
		return Double.valueOf(lon);
	}

	// 緯度をDouble型で返します。
	public Double toLatitude() {
		return Double.valueOf(lat);
	}

	// 指定された住所からGeoLocationを取得します。
	// Google Maps APIを使用して住所をエンコードし、HTTPリクエストを送信します。
	// レスポンスのステータスコードに基づいて処理を行います。
	public static GeoLocation getGeoLocation(String address) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://maps.google.com/maps/geo?q=" + URLEncoder.encode(address, ENCODING)
						+ "&output=csv&key=" + KEY))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String line = response.body();
		GeoLocation location = null;
		int statusCode = Integer.parseInt(line.substring(0, 3));
		if (statusCode == 200) {
			// ステータスコードが200の場合、レスポンスから緯度と経度を抽出します。
			location = new GeoLocation(
					line.substring(CSV_PREFIX.length(), line.indexOf(',', CSV_PREFIX.length())),
					line.substring(line.indexOf(',', CSV_PREFIX.length()) + 1, line.length()));
		} else {
			// ステータスコードに基づいて例外をスローします。
			switch (statusCode) {
				case 400:
					throw new IOException("Bad Request");
				case 500:
					throw new IOException("Unknown error from Google Encoder");
				case 601:
					throw new IOException("Missing query");
				case 602:
					return null;
				case 603:
					throw new IOException("Legal problem");
				case 604:
					throw new IOException("No route");
				case 610:
					throw new IOException("Bad key");
				case 620:
					throw new IOException("Too many queries");
				default:
					throw new IOException("Unexpected status code: " + statusCode);
			}
		}
		return location;
	}
}
