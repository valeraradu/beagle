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

package org.scrutmydocs.webapp.test;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.scrutmydocs.webapp.configuration.ScrutMyDocsProperties;
import org.scrutmydocs.webapp.util.PropertyScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.pilato.spring.elasticsearch.ElasticsearchClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchNodeFactoryBean;

import java.util.Properties;

@Configuration
public class AppTestConfig {

    @Bean
    public ScrutMyDocsProperties smdProperties() throws Exception {
        ScrutMyDocsProperties smdProperties = PropertyScanner.scanPropertyFile();
        return smdProperties;
    }

	@Bean
	public Node esNode() throws Exception {
		ElasticsearchNodeFactoryBean factory = new ElasticsearchNodeFactoryBean();
        Properties props = new Properties();
        props.put("esproperties", "classpath:es-test.properties");
        factory.setProperties(props);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public Client esClient() throws Exception {
		ElasticsearchClientFactoryBean factory = new ElasticsearchClientFactoryBean();
		factory.setNode(esNode());
		factory.afterPropertiesSet();
		return factory.getObject();
    }
}
