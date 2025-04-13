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
package org.springframework.templating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * 単一のリソースからテンプレートインスタンスを作成するStringTemplateファクトリ。
 * 
 * @author Keith Donald
 */
public final class ResourceStringTemplateFactory implements StringTemplateFactory {

	private final org.antlr.stringtemplate.StringTemplate compiledPrototype;

	/**
	 * リソースからStringTemplateインスタンスを作成するStringTemplateFactoryを作成します。
	 * 
	 * @param resource テンプレートテキストが定義されているリソース
	 */
	public ResourceStringTemplateFactory(Resource resource) {
		this.compiledPrototype = createPrototype(resource);
	}

	/**
	 * StringTemplateのインスタンスを取得します。
	 * 
	 * @return 新しいDelegatingStringTemplateインスタンス
	 */
	public StringTemplate getStringTemplate() {
		return new DelegatingStringTemplate(compiledPrototype.getInstanceOf());
	}

	// internal helpers

	/**
	 * リソースからテンプレートのプロトタイプを作成します。
	 * 
	 * @param resource テンプレートテキストが定義されているリソース
	 * @return コンパイルされたStringTemplateプロトタイプ
	 */
	private org.antlr.stringtemplate.StringTemplate createPrototype(Resource resource) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(getResourceReader(resource));
			return loadTemplate(getTemplateName(resource), reader);
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to read template resource " + resource, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Unable to close template resource " + resource + ": " + e.getMessage());
				}
			}
		}
	}

	/**
	 * リソースからリーダーを取得します。
	 * 
	 * @param resource テンプレートテキストが定義されているリソース
	 * @return リソースのリーダー
	 * @throws IOException リソースの読み取りに失敗した場合
	 */
	private Reader getResourceReader(Resource resource) throws IOException {
		if (resource instanceof EncodedResource) {
			return ((EncodedResource) resource).getReader();
		} else {
			return new EncodedResource(resource).getReader();
		}
	}

	/**
	 * リソースのファイル名を取得します。
	 * 
	 * @param resource テンプレートテキストが定義されているリソース
	 * @return リソースのファイル名
	 */
	private String getTemplateName(Resource resource) {
		return resource.getFilename();
	}

	/**
	 * テンプレートをロードします。
	 * 
	 * @param name   テンプレートの名前
	 * @param reader テンプレートの内容を読み込むリーダー
	 * @return ロードされたStringTemplate
	 * @throws IOException テンプレートの読み込みに失敗した場合
	 */
	private org.antlr.stringtemplate.StringTemplate loadTemplate(String name, BufferedReader reader)
			throws IOException {
		String line;
		String nl = System.getProperty("line.separator");
		StringBuilder buf = new StringBuilder(300);
		while ((line = reader.readLine()) != null) {
			buf.append(line);
			buf.append(nl);
		}
		String pattern = buf.toString().trim();
		if (pattern.isEmpty()) {
			return null;
		}
		org.antlr.stringtemplate.StringTemplate template = new org.antlr.stringtemplate.StringTemplate(pattern);
		template.setName(name);
		return template;
	}
}
