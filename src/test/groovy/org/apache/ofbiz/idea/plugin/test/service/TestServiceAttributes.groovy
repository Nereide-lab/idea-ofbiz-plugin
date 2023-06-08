/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.test.service

import org.apache.ofbiz.idea.plugin.dom.ServiceDefFile
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface
import org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker

class TestServiceAttributes extends BaseServiceTest {

    void testVanillaServiceAttributes() {
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        ServiceDefFile.Service service = ps.getService('ping')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, ['hello', 'world'])
        assertContainsElements(optionalAttributes, ['bye'])
    }

    void testEntityAutoAttributesAllOptional() {
        final List EXPECTED_OPTIONALS = ['rifle', 'knife', 'mapId']
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        ServiceDefFile.Service service = ps.getService('createNavezgane')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
    }

    void testEntityAutoAttributes() {
        final List EXPECTED_REQUIRED = ['slaves', 'nemesis']
        final List EXPECTED_OPTIONALS = ['tooth', 'blood', 'magic']
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        ServiceDefFile.Service service = ps.getService('killDracula')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, EXPECTED_REQUIRED)
        assertDoesntContain(requiredAttributes, EXPECTED_OPTIONALS)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
        assertDoesntContain(optionalAttributes, EXPECTED_REQUIRED)
    }

    private static List<String> geRequiredAttrNames(ServiceDefFile.Service service, ProjectServiceInterface ps) {
        ServiceWorker.getRequiredInAttributes(service, ps)
                .stream().map { it.get(ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }

    private static List<String> getOptionalAttrNames(ServiceDefFile.Service service, ProjectServiceInterface ps) {
        ServiceWorker.getOptionalInAttributes(service, ps)
                .stream().map { it.get(ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }
}
