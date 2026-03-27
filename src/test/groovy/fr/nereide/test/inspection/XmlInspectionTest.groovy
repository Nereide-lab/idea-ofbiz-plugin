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
package fr.nereide.test.inspection

import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.xml.EntityNotFoundInXmlInspection
import fr.nereide.inspection.xml.FormNotFoundInFileLocationInspection
import fr.nereide.inspection.xml.LabelNotFoundInXmlInspection
import fr.nereide.inspection.xml.NoAuthOnRequestInspection
import fr.nereide.inspection.xml.ScreenNotFoundInFileLocationInspection
import fr.nereide.inspection.xml.ServiceNotFoundInXmlInspection
import fr.nereide.project.OfbizProjectHelper

/**
 * Inspection tests for Xml
 */
// codenarc-disable JUnitTestMethodWithoutAssert
class XmlInspectionTest extends BaseInspectionTest {

    // =====================================
    // GROOVY TESTS
    // =====================================
    void testNoGroovyServiceFileFoundInspectionFileFix() {
        String intention = message('inspection.location.target.file.not.found.use.quickfix.fixpath')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doInspectionThenQuickFixTestWithFileEdit(true, intention, null)
    }

    void testNoGroovyServiceFileFoundInspectionFileCreate() {
        String intention = message('inspection.location.target.file.not.found.use.quickfix.createfile')
        String description = message('inspection.location.target.file.not.found.display.descriptor')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        String location = 'zelda/webcommon/WEB-INF/zelda-controller.xml'
        doFileInspectionTestWithFileCreation(true, intention, description, location)
    }

    void testNoAuthInControlerInspection() {
        String description = message('inspection.controller.no.auth.descriptor')
        myFixture.enableInspections(new NoAuthOnRequestInspection())
        doAndSetupHighlightTest(true, description)
    }

    // =====================================
    // SCREEN TESTS
    // =====================================
    void testNoScreenFoundInTargetFileInScreenWithLocationAttr() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'HobegobelinScreen'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testNoScreenFoundInTargetFileInScreenWithoutLocationAttr() {
        String elementName = 'SomeOtherRandomScreen'
        doScreenNotFoundTest(true, null, elementName)
    }

    void testNoScreenFoundInTargetFileInForm() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInForm'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testNoScreenFoundInTargetFileInController() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInController'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testScreenIsFoundWithoutWarningInController() {
        doScreenNotFoundTest(false, null, null)
    }

    void testDynamicScreenNameIsIgnored() {
        doScreenNotFoundTest(false, null, null)
    }

    void testScreenIsFoundInCompoundFileFromScreen() {
        doMove()
        doScreenNotFoundTest(false, null, null)
    }

    void testScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName() {
        doMove()
        String location =
                'zelda/widget/ScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName.xml'
        String elementName = 'ILikeFairPhone'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testScreenAtDynamicLocationIsNoError() {
        doScreenNotFoundTest(false, null, null)
    }

    // =====================================
    // FORMS TESTS
    // =====================================
    void testNoFormFoundInTargetFileInForm() {
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'MyZeldaFoesFormNotFound'
        doFormNotFoundTest(true, location, elementName)
    }

    void testNoFormFoundInSameFileInForm() {
        String elementName = 'IWantAFormInThisFile'
        doFormNotFoundTest(true, null, elementName)
    }

    void testNoFormFoundInTargetFileInScreen() {
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'AFormInADistantFileFormScreen'
        doFormNotFoundTest(true, location, elementName)
    }

    void testDynamicFormNameIsIgnored() {
        doFormNotFoundTest(false, null, null)
    }

    // =====================================
    // UILABEL TESTS
    // =====================================
    void testLabelNotFoundInScreenFile() {
        String desc = message('inspection.label.not.found.display.descriptor')
        String intention = message('inspection.label.not.found.quickfix.create')
        myFixture.enableInspections(new LabelNotFoundInXmlInspection())
        String file = "${this.getTestName(false)}.xml"
        String dest = 'zelda/widget'
        myFixture.moveFile("xml/$file", dest)
        myFixture.configureByFile("$dest/$file")
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        List<String> highlightDescs = highlightInfos*.description
        assert !highlightInfos.empty
        assert highlightDescs.contains(desc)
        final IntentionAction action = myFixture.findSingleIntention(intention)
        assert action
        myFixture.launchAction(action)
        assert OfbizProjectHelper.getInstance(myFixture.project).getProperty('notExistingLabel')
    }

    void testNoInspectionOnFirstOfMultipleLabels() {
        myFixture.configureByFile(testFile)
        ['FirstProperty', 'SecondProperty', 'ThirdProperty', 'FourthProperty', 'FifthProperty'].forEach {
            label -> assert OfbizProjectHelper.getInstance(myFixture.project).getProperty(label)
        }
        String desc = message('inspection.label.not.found.display.descriptor')
        myFixture.enableInspections(new LabelNotFoundInXmlInspection())
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        List<String> descriptions = highlightInfos*.description
        assert !(descriptions.contains(desc))
    }

    // =====================================
    // SERVICES TESTS
    // =====================================
    void testServiceNotFoundInspection() {
        doHighlightTest(true, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInXmlInspection())
    }

    void testServiceNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInXmlInspection())
    }

    // =====================================
    // ENTITY TESTS
    // =====================================
    void testEntityNotFoundInspection() {
        doHighlightTest(true, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInXmlInspection())
    }

    void testEntityNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInXmlInspection())
    }

    protected String getLang() { return 'xml' }

    private void doFormNotFoundTest(boolean mustFind, String location, String elName) {
        myFixture.enableInspections(new FormNotFoundInFileLocationInspection())
        doInspectionThenQuickFixWithXmlElementCreate(mustFind,
                message('inspection.form.not.found.on.target.use.quickfix.create'),
                message('inspection.form.not.found.on.target.display.descriptor'),
                location, elName, 'form')
    }

    private void doScreenNotFoundTest(boolean mustFind, String location, String elName) {
        myFixture.enableInspections(new ScreenNotFoundInFileLocationInspection())
        doInspectionThenQuickFixWithXmlElementCreate(mustFind,
                message('inspection.screen.not.found.on.target.use.quickfix.create'),
                message('inspection.screen.not.found.on.target.display.descriptor'),
                location, elName, 'screen')
    }

}
