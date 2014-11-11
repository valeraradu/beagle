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

package org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data;

import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.DropBoxRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.util.StringTools;




/**
 * Filesystem Rivers metadata Abstraction
 * @author PILATO
 * @see FSRiver
 * @see DropBoxRiver
 */
public class AbstractFSRiver extends BasicRiver {
	private static final long serialVersionUID = 1L;
	
	private String url;
	private Long updateRate;
	private String includes;
	private String excludes;
	private String analyzer;

	/**
	 * We implement here a "abstractfs" river
	 */
	@Override
	public String getType() {
		return "abstractfs";
	}

	public AbstractFSRiver() {
		this("tmp", "/tmp", 60L);
	}
	
	/**
	 * @param id The unique id of this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public AbstractFSRiver(String id, String url, Long updateRate) {
		super(id);
		this.url = url;
		this.updateRate = updateRate;
		this.analyzer = "standard";
	}


	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 * @param analyzer Analyzer to use
	 * @param started Is the river already started ?
	 */
	public AbstractFSRiver(String id, String indexname, String typename, String name,
			String url, Long updateRate, String analyzer, boolean started) {
		this(id, indexname, typename, name, url, updateRate, null, null, analyzer, started);
	}

	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 * @param includes Include list (comma separator)
	 * @param excludes Exclude list (comma separator)
	 * @param analyzer Analyzer to use
	 * @param started Is the river already started ?
	 */
	public AbstractFSRiver(String id, String indexname, String typename, String name,
			String url, Long updateRate, String includes, String excludes, String analyzer, boolean started) {
		super(id, indexname, typename, name, started);
		this.url = url;
		this.updateRate = updateRate;
		this.includes = includes;
		this.excludes = excludes;
		this.analyzer = analyzer;
	}

	/**
	 * @return URL where to fetch content
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url URL where to fetch content
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return Update Rate (in seconds)
	 */
	public Long getUpdateRate() {
		return updateRate;
	}
	
	/**
	 * @param updateRate Update Rate (in seconds)
	 */
	public void setUpdateRate(Long updateRate) {
		this.updateRate = updateRate;
	}

	/**
	 * @return Include list (comma separator)
	 */
	public String getIncludes() {
		return includes;
	}

	/**
	 * @param includes Include list (comma separator)
	 */
	public void setIncludes(String includes) {
		this.includes = includes;
	}

	/**
	 * @return Exclude list (comma separator)
	 */
	public String getExcludes() {
		return excludes;
	}

	/**
	 * @param excludes Exclude list (comma separator)
	 */
	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}
	
	@Override
	public String toString() {
		return StringTools.toString(this);
	}

}
