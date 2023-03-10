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

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import fr.nereide.reference.common.ServiceReference
import fr.nereide.reference.xml.DatasourceReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.GroovyServiceDefReference
import fr.nereide.reference.xml.JavaMethodReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.RequestMapReference
import fr.nereide.reference.xml.ScreenReference
import fr.nereide.reference.xml.ViewMapReference

class TestReferenceInXml extends BaseReferenceTestCase {

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/xml"
    }

//    void testScreenNotFoundReferenceInCurrentFile() {
//        String file = 'xml/ScreenNotFoundReferenceInCurrentFile.xml'
//        myFixture.configureByFile(file)
//        PsiReference ref = myFixture.getReferenceAtCaretPosition(file)
//        assertNull ref.resolve()
//    }
//
//    void testScreenNotFoundReferenceInDistantFile() {
//        String file = 'xml/ScreenNotFoundReferenceInDistantFile.xml'
//        myFixture.configureByFile(file)
//        PsiReference ref = myFixture.getReferenceAtCaretPosition(file)
//        assertNull ref.resolve()
//    }

    void testScreenInCurrentFileReference() {
        doTest(ScreenReference.class, 'FindFacility', false)
    }

    void testScreenInDistantFileReference() {
        doTest(ScreenReference.class, 'viewprofile', false)
    }

    void testScreenReferenceFromViewMap() {
        doTest(ScreenReference.class, 'viewprofile', false)
    }

    void testFormReferenceFromScreen() {
        doTest(FormReference.class, 'FooForm')
    }

    void testFileReferenceFromScreen() {
        doTest(FileReference.class, 'FooScript', false)
    }

    void testFormReferenceFromForm() {
        doTest(FormReference.class, 'FooForm')
    }

    void testRequestMapReferenceFromForm() {
        doTest(RequestMapReference.class, 'fooRequest')
    }

    void testViewMapReferenceFromRequestMap() {
        doTest(ViewMapReference.class, 'MyHome')
    }

    void testMenuReferenceFromScreen() {
        doTest(MenuReference.class, 'FooMenu')
    }

    void testGroovyMethodReferenceFromServiceDef() {
        doTest(GroovyServiceDefReference.class, 'fooGroovyService')
    }

    void testJavaMethodReferenceFromServiceDef() {
        doTest(JavaMethodReference.class, 'createNote')
    }

    void testServiceDefReferenceFromServiceGroup() {
        doTest(ServiceReference.class, 'SmileSmileSmile')
    }

    void testDataSourceReferenceFromDelegator() {
        doTest(DatasourceReference.class, 'localderby')
    }
}