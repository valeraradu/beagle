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

package org.scrutmydocs.webapp.api.index.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.index.data.Index;
import org.scrutmydocs.webapp.api.index.data.RestResponseIndex;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.index.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Index RESTFul API
 * <ul>
 * <li>PUT : create a new index to hold documents
 * <li>DELETE : remove an index
 * </ul>
 * @author David Pilato
 *
 */
@Controller
@RequestMapping("/1/index")
public class IndexApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	IndexService indexService;

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[4];
		apis[0] = new Api("/1/index", "POST", "Create a new Index. You have to post settings.");
		apis[1] = new Api("/1/index/{index}", "POST", "Create a new Index named {index}. You can post settings.");
		apis[2] = new Api("/1/index/{index}/{type}", "POST", "Create a new Index named {index}/{type}. You can post settings.");
		apis[3] = new Api("/1/index/{index}", "DELETE", "Delete an existing index. Use with caution !");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /1/index API helps you to manage your indices.";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{index}/{type}")
	public @ResponseBody
	RestResponseIndex create(@PathVariable String index, 
			@PathVariable String type,
			@RequestBody Index settings) {
		try {
			String analyzer = null;
			
			if (settings != null) {
				if (index == null) {
					index = settings.getIndex();
				} else {
					settings.setIndex(index);
				}
				if (type == null) {
					type = settings.getType();
				} else {
					settings.setType(type);
				}
				analyzer = settings.getAnalyzer();
			}
			indexService.createIndex(index, type, analyzer);
		} catch (RestAPIException e) {
			return new RestResponseIndex(e);
		}
		
		return new RestResponseIndex(settings);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{index}")
	public @ResponseBody
	RestResponseIndex create(@PathVariable String index, 
			@RequestBody Index settings) {
		return create(index, SMDSearchProperties.INDEX_TYPE_DOC, settings);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	RestResponseIndex create(@RequestBody Index settings) {
		return create(null, null, settings);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{index}")
	public @ResponseBody
	RestResponseIndex delete(@PathVariable String index) {
		try {
			indexService.delete(index);
		} catch (RestAPIException e) {
			return new RestResponseIndex(e);
		}
		return new RestResponseIndex();
	}

}
