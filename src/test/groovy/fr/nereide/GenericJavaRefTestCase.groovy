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

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

class GenericJavaRefTestCase extends LightJavaCodeInsightFixtureTestCase {

    PsiClass addEntityQuery() {
        myFixture.addClass("package org.apache.ofbiz.entity.util;" +
                " public class EntityQuery {" +
                " static void use(){ return null;}" +
                " static void from(){ return null;}" +
                "}")
    }

    PsiClass addDynamicEntity() {
        myFixture.addClass("package org.apache.ofbiz.entity.model;" +
                " public class DynamicViewEntity {" +
                " static void addMemberEntity(){ return null;}" +
                "}")
    }

    PsiClass addDelegator() {
        myFixture.addClass("package org.apache.ofbiz.entity;" +
                " public interface Delegator {" +
                " static void find(){ return null;}" +
                " static void findOne(){ return null;}" +
                " static void findList(){ return null;}" +
                " static void findAll(){ return null;}" +
                "}")
    }

    PsiReference setupFixtureForTestAndGetRef(String testFolder) {
        myFixture.copyDirectoryToProject(testFolder, '')
        myFixture.configureByFile("${testFolder}/MyTestClass.java")
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion("${testFolder}/MyTestClass.java")
        return ref
    }
}
