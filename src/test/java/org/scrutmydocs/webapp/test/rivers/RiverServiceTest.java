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

package org.scrutmydocs.webapp.test.rivers;

import fr.pilato.elasticsearch.river.fs.river.FsRiver;
import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.helper.FSRiverHelper;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;
import org.springframework.beans.factory.annotation.Autowired;


public class RiverServiceTest extends AbstractConfigurationTest {

	@Autowired RiverService riverService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		FSRiver fsriver = new FSRiver("mytestriver", SMDSearchProperties.INDEX_NAME,
                SMDSearchProperties.INDEX_TYPE_DOC, "tmp",
                FsRiver.PROTOCOL.LOCAL, null, null, null,
                "/tmp_es", 30L, null, null, "standard", false);

		riverService.start(fsriver, new FSRiverHelper().toXContent(fsriver));
	}
	
	@Test public void test_remove_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		FSRiver fsriver = new FSRiver("mytestriver", SMDSearchProperties.INDEX_NAME,
                SMDSearchProperties.INDEX_TYPE_DOC, "tmp",
                FsRiver.PROTOCOL.LOCAL, null, null, null,
                "/tmp_es", 30L, null, null, "standard", false);

		riverService.start(fsriver, new FSRiverHelper().toXContent(fsriver));
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		riverService.stop(fsriver);
	}

}
