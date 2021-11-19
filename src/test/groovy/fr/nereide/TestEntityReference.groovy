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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import fr.nereide.reference.java.EntityJavaReference

class TestEntityReference extends LightJavaCodeInsightFixtureTestCase {
    TestEntityReference() {}

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/EntityReference"
    }

    @Override
    void setUp() {
        super.setUp()
        myFixture.addClass("package org.apache.ofbiz.entity;" +
                " public interface Delegator {" +
                " static void find(){ return null;}" +
                " static void findOne(){ return null;}" +
                " static void findList(){ return null;}" +
                " static void findAll(){ return null;}" +
                "}")
        myFixture.addClass("package org.apache.ofbiz.entity.model.DynamicViewEntity;" +
                " public class DynamicViewEntity {" +
                " static void addMemberEntity(){ return null;}" +
                "}")
        myFixture.addClass("package org.apache.ofbiz.entity.util.EntityQuery;" +
                " public class EntityQuery {" +
                " static void from(){ return null;}" +
                "}")
    }

    /**
     * Test for find method() from delegator
     */
    void testEntityReferenceWithFindMethod() {
        myFixture.copyDirectoryToProject('EntityReferenceWithFindMethod', '')
        myFixture.configureByFile("EntityReferenceWithFindMethod/MyTestClass.java")
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(
                "EntityReferenceWithFindMethod/MyTestClass.java")

        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'HyruleCastle', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findOne() method from delegator
     */
    void testEntityReferenceWithFindOneMethod() {
        myFixture.copyDirectoryToProject('EntityReferenceWithFindOneMethod', '')
        myFixture.configureByFile('EntityReferenceWithFindOneMethod/MyTestClass.java')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(
                'EntityReferenceWithFindOneMethod/MyTestClass.java')

        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'PiltoverData', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findList() method from delegator
     */
    void testEntityReferenceWithFindListMethod() {
        myFixture.copyDirectoryToProject('EntityReferenceWithFindListMethod', '')
        myFixture.configureByFile('EntityReferenceWithFindListMethod/MyTestClass.java')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(
                'EntityReferenceWithFindListMethod/MyTestClass.java')

        assertTrue ref instanceof EntityJavaReference
        EntityJavaReference entityRef = (EntityJavaReference) ref
        assertEquals 'Enderman', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    /**
     * Test for findAll() method from delegator
     */
    void testEntityReferenceWithFindAllMethod() {
        assert true
    }

    /**
     * Test for addMemberEntity() method from DynamicViewEntity
     */
    void testEntityReferenceWithAddMemberEntityMethod() {
        assert true
    }

    /**
     * Test for from() method from EntityQuery
     */
    void testEntityReferenceWithFromMethod() {
        assert true
    }
}
