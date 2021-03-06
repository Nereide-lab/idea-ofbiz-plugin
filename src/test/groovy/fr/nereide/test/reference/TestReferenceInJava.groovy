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

import com.intellij.psi.PsiReference
import fr.nereide.reference.java.EntityJavaReference

class TestReferenceInJava extends GenericRefTestCase {

    /**
     * Test for find method() from delegator
     */
    void testEntityReferenceWithFindMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithFindMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'HyruleCastle', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findOne() method from delegator
     */
    void testEntityReferenceWithFindOneMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithFindOneMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'PiltoverData', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findList() method from delegator
     */
    void testEntityReferenceWithFindListMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithFindListMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'Enderman', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findAll() method from delegator
     */
    void testEntityReferenceWithFindAllMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithFindAllMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'TwoFlowers', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for addMemberEntity() method from DynamicViewEntity
     */
    void testEntityReferenceWithAddMemberEntityMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithAddMemberEntityMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'WeWereOnABreak', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for from() method from EntityQuery
     */
    void testEntityReferenceWithFromMethod() {
        PsiReference ref = setupFixtureForTestAndGetRef('java/EntityReferenceWithFromMethod.java')
        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'Rick', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }
}
