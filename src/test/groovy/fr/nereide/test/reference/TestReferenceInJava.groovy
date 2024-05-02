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

class TestReferenceInJava extends BaseReferenceTestCase {

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/java"
    }

    @Override
    protected String getExtension() { return 'java' }

    void testEntityReferenceWithFindMethod() {
        doTest(EntityReference.class, 'HyruleCastle')
    }

    void testEntityReferenceWithFindOneMethod() {
        doTest(EntityReference.class, 'PiltoverData')
    }

    void testEntityReferenceWithFindListMethod() {
        doTest(EntityReference.class, 'Enderman')
    }

    void testEntityReferenceWithFindAllMethod() {
        doTest(EntityReference.class, 'TwoFlowers')
    }

    void testEntityReferenceWithAddMemberEntityMethod() {
        doTest(EntityReference.class, 'WeWereOnABreak')
    }

    void testEntityReferenceWithFromMethod() {
        doTest(EntityReference.class, 'Rick')
    }

    void testEntityReferenceWithMakeValueMethod() {
        doTest(EntityReference.class, 'PickleRick')
    }

    void testServiceReferenceWithRunSync() {
        doTest(ServiceReference.class, 'pivot')
    }

    void testServiceReferenceWithMakeValidContext() {
        doTest(ServiceReference.class, 'pivot')
    }

    void testServiceReferenceWithSchedule() {
        doTest(ServiceReference.class, 'pivot')
    }

    void testEntityReferenceWithGetRelatedMethodWithoutTitleOnTargetEntity() {
        doTest(EntityReference.class, 'Matata2')
    }

    void testEntityReferenceWithGetRelatedMethodWithTitleOnTargetEntity() {
        doTest(EntityReference.class, 'Matata3', false)
    }
}
