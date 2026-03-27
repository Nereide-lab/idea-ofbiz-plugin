/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import fr.nereide.reference.common.ServiceReference
import fr.nereide.reference.common.UiLabelReference
import fr.nereide.reference.xml.DatasourceReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.GroovyServiceDefReference
import fr.nereide.reference.xml.JavaMethodReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.RequestMapReference
import fr.nereide.reference.xml.ScreenReference
import fr.nereide.reference.xml.EngineReference
import fr.nereide.reference.xml.ViewMapReference

/**
 * Reference tests in xml
 */
// codenarc-disable DuplicateStringLiteral
class TestReferenceInXml extends BaseReferenceTestCase {

    void testScreenNotFoundReferenceInCurrentFile() {
        doTest(false)
    }

    void testScreenNotFoundReferenceInDistantFile() {
        doTest(false)
    }

    void testScreenInCurrentFileReference() {
        doTest(ScreenReference, 'FindFacility')
    }

    void testScreenInDistantFileReference() {
        doTest(ScreenReference, 'viewprofile')
    }

    void testScreenReferenceFromViewMap() {
        doTest(ScreenReference, 'viewprofile')
    }

    void testFormReferenceFromScreen() {
        doTest(FormReference, 'FooForm')
    }

    void testFileReferenceFromScreen() {
        doTest(FileReference, 'FooScript')
    }

    void testFileReferenceFromThemeFile() {
        doTest(FileReference, 'AFile')
    }

    void testFormReferenceFromForm() {
        doTest(FormReference, 'FooForm')
    }

    void testRequestMapReferenceFromForm() {
        doTest(RequestMapReference, 'fooRequest')
    }

    void testRequestMapReferenceFromLookup() {
        doTest(RequestMapReference, 'fooRequest')
    }

    void testViewMapReferenceFromRequestMap() {
        doTest(ViewMapReference, 'MyHome')
    }

    void testMenuReferenceFromScreen() {
        doTest(MenuReference, 'FooMenu')
    }

    void testGroovyMethodReferenceFromServiceDef() {
        doTest(GroovyServiceDefReference, 'fooGroovyService')
    }

    void testJavaMethodReferenceFromServiceDef() {
        doTest(JavaMethodReference, 'createNote')
    }

    void testServiceDefReferenceFromServiceGroup() {
        doTest(ServiceReference, 'SmileSmileSmile')
    }

    void testDataSourceReferenceFromDelegator() {
        doTest(DatasourceReference, 'localderby')
    }

    void testServiceDefReferenceWithMultipleImplementations() {
        doTest(ServiceReference, 'MidnightInMe', true)
    }

    void testUiLabelPropertyInTitle() {
        doTest(UiLabelReference, 'Baptiste')
    }

    void testUiLabelPropertyInTitleWithOtherString() {
        doTest(UiLabelReference, 'PasBaptiste')
    }

    void testUiLabelPropertyInTitleAfterAnOtherProperty() {
        doTest(UiLabelReference, 'Baptiste')
    }

    void testUiLabelPropertyInTitleAfterFourOtherProperty() {
        doTest(UiLabelReference, 'FifthProperty')
    }

    void testServiceReferenceInXmlDataFile() {
        doTest(ServiceReference, 'Croissance3000Service')
    }

    void testServiceEngineReferenceTest() {
        doTest(EngineReference, 'entity-auto')
    }

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/xml"
    }

}
