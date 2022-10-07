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
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.reference.xml.ControllerRequestReference
import fr.nereide.reference.xml.ControllerViewReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.ScreenReference

class TestReferenceInXml extends GenericRefTestCase {

    void testScreenInCurrentFileReference() {
        String file = 'xml/ScreenInCurrentFileReference.xml'
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        assertEquals 'FindFacility', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenInDistantFileReference() {
        String file = 'xml/ScreenInDistantFileReference.xml'
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        assertEquals 'viewprofile', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenNotFoundReferenceInCurrentFile() {
        String file = 'xml/ScreenNotFoundReferenceInCurrentFile.xml'
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPosition(file)
        assertNull ref.resolve()
    }

    void testScreenNotFoundReferenceInDistantFile() {
        String file = 'xml/ScreenNotFoundReferenceInDistantFile.xml'
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPosition(file)
        assertNull ref.resolve()
    }

    void testScreenReferenceFromViewMap() {
        String file = "xml/ScreenReferenceFromViewMap.xml"
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        assert ref.getElement().getText().contains('viewprofile')
        // this one is a pain..
        assertNotNull((ref as PsiMultiReference).getReferences().find { it instanceof ScreenReference })
        assertNotNull ref.resolve()
    }

    void testFormReferenceFromScreen() {
        String file = "xml/FormReferenceFromScreen.xml"
        configureAndAssertRefAndRefType(file, 'FooForm', FormReference.class)
    }

    void testFormReferenceFromForm() {
        String file = "xml/FormReferenceFromForm.xml"
        configureAndAssertRefAndRefType(file, 'FooForm', FormReference.class)
    }

    void testRequestMapReferenceFromForm() {
        String file = "xml/RequestMapReferenceFromForm.xml"
        configureAndAssertRefAndRefType(file, 'fooRequest', ControllerRequestReference.class)
    }

    void testViewMapReferenceFromRequestMap() {
        String file = "xml/ViewMapReferenceFromRequestMap.xml"
        configureAndAssertRefAndRefType(file, 'MyHome', ControllerViewReference.class)
    }

    void testMenuReferenceFromScreen() {
        String file = "xml/MenuReferenceFromScreen.xml"
        configureAndAssertRefAndRefType(file, 'FooMenu', MenuReference.class)
    }

    private void configureAndAssertRefAndRefType(String file, String elementName, Class refClass) {
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        assertEquals elementName, ref.getElement().getName() as String
        assert refClass.isAssignableFrom(refClass) // instanceof like
        assertNotNull ref.resolve()
    }
}