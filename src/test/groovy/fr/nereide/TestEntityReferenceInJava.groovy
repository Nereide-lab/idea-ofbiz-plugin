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

package fr.nereide


import com.intellij.psi.PsiReference
import fr.nereide.reference.java.EntityJavaReference

class TestEntityReferenceInJava extends GenericRefTestCase {
    TestEntityReferenceInJava() {}

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/JavaEntityReference"
    }

    /**
     * Test for find method() from delegator
     */
    void testEntityReferenceWithFindMethod() {
        addDelegator()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithFindMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'HyruleCastle', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findOne() method from delegator
     */
    void testEntityReferenceWithFindOneMethod() {
        addDelegator()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithFindOneMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'PiltoverData', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findList() method from delegator
     */
    void testEntityReferenceWithFindListMethod() {
        addDelegator()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithFindListMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'Enderman', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findAll() method from delegator
     */
    void testEntityReferenceWithFindAllMethod() {
        addDelegator()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithFindAllMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'TwoFlowers', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for addMemberEntity() method from DynamicViewEntity
     */
    void testEntityReferenceWithAddMemberEntityMethod() {
        addDynamicEntity()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithAddMemberEntityMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'WeWereOnABreak', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for from() method from EntityQuery
     */
    void testEntityReferenceWithFromMethod() {
        addEntityQuery()
        PsiReference ref = setupFixtureForTestAndGetRef('EntityReferenceWithFromMethod', 'java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'Rick', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }
}
