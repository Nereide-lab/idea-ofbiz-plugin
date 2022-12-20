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

class TestReferenceInJava extends GenericRefTestCase {

    /**
     * Test for find method() from delegator
     */
    void testEntityReferenceWithFindMethod() {
        String file = 'java/EntityReferenceWithFindMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'HyruleCastle')
    }

    /**
     * Test for findOne() method from delegator
     */
    void testEntityReferenceWithFindOneMethod() {
        String file = 'java/EntityReferenceWithFindOneMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'PiltoverData')
    }

    /**
     * Test for findList() method from delegator
     */
    void testEntityReferenceWithFindListMethod() {
        String file = 'java/EntityReferenceWithFindListMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'Enderman')
    }

    /**
     * Test for findAll() method from delegator
     */
    void testEntityReferenceWithFindAllMethod() {
        String file = 'java/EntityReferenceWithFindAllMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'TwoFlowers')
    }

    /**
     * Test for addMemberEntity() method from DynamicViewEntity
     */
    void testEntityReferenceWithAddMemberEntityMethod() {
        String file = 'java/EntityReferenceWithAddMemberEntityMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'WeWereOnABreak')
    }

    /**
     * Test for from() method from EntityQuery
     */
    void testEntityReferenceWithFromMethod() {
        String file = 'java/EntityReferenceWithFromMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'Rick')
    }

    /**
     * Test for makeValue() method from delegator
     */
    void testEntityReferenceWithMakeValueMethod() {
        String file = 'java/EntityReferenceWithMakeValueMethod.java'
        configureByFileAndTestRefTypeAndValue(file, EntityReference.class, 'PickleRick')
    }
}
