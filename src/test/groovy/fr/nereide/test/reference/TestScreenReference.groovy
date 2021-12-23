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
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

class TestScreenReference extends LightJavaCodeInsightFixtureTestCase {
    TestScreenReference() {}

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/ScreenReference"
    }

    void testScreenInCurrentFileReference() {
        myFixture.copyDirectoryToProject('ScreenInCurrentFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion("testDataRefScreenInCurrentFile.xml")
        assertEquals'FindFacility', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenInDistantFileReference() {
        myFixture.copyDirectoryToProject('ScreenInDistantFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion("commonext/widget/ofbizsetup/SetupScreens.xml")
        assertEquals 'viewprofile', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenNotFoundReferenceInCurrentFile() {
        myFixture.copyDirectoryToProject('ScreenNotFoundInCurrentFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPosition("testDataRefScreenInCurrentFile.xml")
        assertNull ref.resolve()
    }

    void testScreenNotFoundReferenceInDistantFile() {
        myFixture.copyDirectoryToProject('ScreenNotFoundInDistantFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPosition("commonext/widget/ofbizsetup/SetupScreens.xml")
        assertNull ref.resolve()
    }
}
