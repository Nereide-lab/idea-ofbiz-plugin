/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.test.reference

import fr.nereide.reference.common.EntityReference
import fr.nereide.reference.common.ServiceReference

/**
 * Reference tests in groovy
 */
// codenarc-disable DuplicateStringLiteral
class TestReferenceInGroovy extends BaseReferenceTestCase {

    void testGroovyEntityReferenceWithFindMethod() {
        doTest(EntityReference, 'Lobster')
    }

    void testGroovyViewEntityReferenceWithFindMethod() {
        doTest(EntityReference, 'Zaun')
    }

    void testGroovyEntityReferenceWithFromMethod() {
        doTest(EntityReference, 'PickleRick')
    }

    void testGroovyServiceReferenceWithRunCall() {
        doTest(ServiceReference, 'pivot')
    }

    void testEntityReferenceWithMakeValueMethod() {
        doTest(EntityReference, 'Zaun')
    }

    void testEntityReferenceWithGetRelatedMethodWithoutTitleOnTargetEntity() {
        doTest(EntityReference, 'Matata')
    }

    void testEntityReferenceWithGetRelatedMethodWithTitleOnTargetEntity() {
        doTest(EntityReference, 'Matata1')
    }

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/groovy"
    }

    @Override
    protected String getExtension() { return 'groovy' }

}
