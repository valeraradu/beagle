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

package org.scrutmydocs.webapp.service.settings.rivers;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component
public abstract class AdminRiverAbstractService<T extends BasicRiver> implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;
	@Autowired RiverService riverService;

	abstract public AbstractRiverHelper<T> getHelper();
	abstract public T buildInstance();
	
	/**
	 * Get the river definition by its name
	 * @param name
	 * @return
	 */
	public T get(String name) {
		if (logger.isDebugEnabled()) logger.debug("get({})", name);
		T river = null;
		
		if (name != null) {
			GetRequestBuilder rb = new GetRequestBuilder(client, SMDSearchProperties.ES_META_INDEX);
			rb.setType(SMDSearchProperties.ES_META_RIVERS);
			rb.setId(name);
			
			try {
				GetResponse response = rb.execute().actionGet();
				if (response.isExists()) {
					river = getHelper().toRiver(buildInstance(), response.getSourceAsMap());
				}
			} catch (IndexMissingException e) {
				// Index does not exists, so RIVER does not exist...
			}
		}

		if (logger.isDebugEnabled()) logger.debug("/get({})={}", name, river);
		return river;
	}

	/**
	 * Get all active rivers
	 * @return
	 */
	public List<T> get() {
		if (logger.isDebugEnabled()) logger.debug("get()");
		List<T> rivers = new ArrayList<T>();
		
		SearchRequestBuilder srb = new SearchRequestBuilder(client);

		try {
			srb.setIndices(SMDSearchProperties.ES_META_INDEX);
			srb.setTypes(SMDSearchProperties.ES_META_RIVERS);

            // We need to filter for our rivers only
            if (getHelper().type() != null) {
                srb.setFilter(FilterBuilders.termFilter("type",getHelper().type()));
            }

			SearchResponse response = srb.execute().actionGet();
			
			if (response.getHits().totalHits() > 0) {
				
				for (int i = 0; i < response.getHits().hits().length; i++) {
					T river = buildInstance();

					SearchHit hit = response.getHits().hits()[i];

					// We only manage rivers for type getHelper().type()
					river = getHelper().toRiver(river, hit.sourceAsMap());

					if (river.getType().equals(getHelper().type())) {
						// For each river, we check if the river is started or not
						river.setStart(riverService.checkState(river));
						rivers.add(river);
					}
				}
			}
			
		} catch (IndexMissingException e) {
			// That's a common use case. We started with an empty index
		}
		
		if (logger.isDebugEnabled()) logger.debug("/get()={}", rivers);
		return rivers;
	}

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void update(T river) {
		if (logger.isDebugEnabled()) logger.debug("update({})", river);
		XContentBuilder xb = getHelper().toXContent(river);		
		
		client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, river.getId()).setSource(xb).setRefresh(true)
				.execute().actionGet();
		
		if (logger.isDebugEnabled()) logger.debug("/update({})", river);
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void remove(T river) {
		if (logger.isDebugEnabled()) logger.debug("remove({})", river);
		
		// We stop the river if running
		if (riverService.checkState(river)) {
			riverService.stop(river);
		}

		// We remove the river in the database
		client.prepareDelete(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, river.getId()).execute().actionGet();
			
		if (logger.isDebugEnabled()) logger.debug("/remove({})", river);
	}

	/**
	 * Start a river
	 * @param river
	 */
	public void start(T river) {
		if (logger.isDebugEnabled()) logger.debug("start({})", river);
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		riverService.start(river, getHelper().toXContent(river));
		
		if (logger.isDebugEnabled()) logger.debug("/start({})", river);		
	}
	
	/**
	 * Stop a river
	 * @param river
	 */
	public void stop(T river) {
		if (logger.isDebugEnabled()) logger.debug("stop({})", river);
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		riverService.stop(river);
		
		if (logger.isDebugEnabled()) logger.debug("/stop({})", river);		
	}
	
	
}
