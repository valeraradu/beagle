/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.scrutmydocs.webapp.itest.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.Base64;
import org.junit.Test;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.api.document.data.RestResponseDocument;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.util.ESHelper;


/**
 * Test for module "1/doc/"
 * @author David Pilato
 */
public class DocumentApiTest extends AbstractApiTest {
	
	/**
	 * Module is "1/doc/"
	 */
	@Override
	protected String getModuleApiUrl() {
		return "1/doc/";
	}

	@Test
	public void push_document() throws Exception {
		String content = ESHelper.readFileInClasspath("/integration/docs/LICENSE.txt");
		String base64Content = Base64.encodeBytes(content.getBytes());
		
		Document input = new Document("nom.pdf", base64Content);

		RestResponseDocument response = restTemplate.postForObject(buildFullApiUrl(),
				input, RestResponseDocument.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Document output = (Document) response.getObject();
		assertNotNull(output);
		assertNotNull(output.getId());
		assertEquals(SMDSearchProperties.INDEX_NAME, output.getIndex());
		assertEquals(SMDSearchProperties.INDEX_TYPE_DOC, output.getType());
		input.setId(output.getId());
		assertEquals(input, output);
	}

	@Test
	public void push_then_get_document() throws Exception {
		String content = ESHelper.readFileInClasspath("/integration/docs/LICENSE.txt");
		String base64Content = Base64.encodeBytes(content.getBytes());
		
		Document input = new Document("nom2.pdf", base64Content);
		
		RestResponseDocument response = restTemplate.postForObject(buildFullApiUrl(),
				input, RestResponseDocument.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		input = (Document) response.getObject();

		String url = buildFullApiUrl(input.getIndex() + "/" + input.getType() + "/"
				+ input.getId());

		Document output = restTemplate.getForObject(url, Document.class);

		assertNotNull(output);

		assertEquals(input, output);
	}

}
