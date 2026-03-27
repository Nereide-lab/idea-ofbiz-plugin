/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.test.service

import fr.nereide.dom.element.service.Service
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.worker.ServiceWorker

/**
 * Service worker tests
 */
class TestServiceAttributes extends BaseServiceTest {

    void testVanillaServiceAttributes() {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('ping')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertContainsElements(requiredAttributes, ['hello', 'world'])
        assertContainsElements(optionalAttributes, ['bye'])
    }

    void testEntityAutoAttributesAllOptional() {
        final List expectedOptionals = ['rifle', 'knife', 'mapId']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('createNavezgane')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, expectedOptionals)
    }

    void testEntityAutoAttributes() {
        final List expectedRequired = ['slaves', 'nemesis']
        final List expectedOptionals = ['tooth', 'blood', 'magic']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('killDracula')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertContainsElements(requiredAttributes, expectedRequired)
        assertDoesntContain(requiredAttributes, expectedOptionals)
        assertContainsElements(optionalAttributes, expectedOptionals)
        assertDoesntContain(optionalAttributes, expectedRequired)
    }

    private static List<String> getRequiredAttrNames(Service service, OfbizProjectHelper ph) {
        return ServiceWorker.getRequiredInParameters(service, ph)*.get(ServiceWorker.SERVICE_ATTR_NAME) as List<String>
    }

    private static List<String> getOptionalAttrNames(Service service, OfbizProjectHelper ph) {
        return ServiceWorker.getOptionalInParameters(service, ph)*.get(ServiceWorker.SERVICE_ATTR_NAME) as List<String>
    }

}
