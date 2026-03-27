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

import com.intellij.codeInspection.LocalInspectionTool
import fr.nereide.inspection.xml.DuplicatedEntityXmlInspection
import fr.nereide.inspection.xml.DuplicatedFormInspection
import fr.nereide.inspection.xml.DuplicatedMenuInspection
import fr.nereide.inspection.xml.DuplicatedScreenInspection
import fr.nereide.inspection.xml.DuplicatedServiceXmlInspection

/**
 * Inspection tests for duplicates in Xml
 */
// codenarc-disable JUnitTestMethodWithoutAssert
class XmlDuplicateInspectionTest extends BaseInspectionTest {

    static final private String FORM_DESC = message('inspection.form.duplicate.display.descriptor')
    static final private String SCREEN_DESC = message('inspection.screen.duplicate.display.descriptor')
    static final private String MENU_DESC = message('inspection.menu.duplicate.display.descriptor')
    static final private String SERVICE_DESC = message('inspection.service.duplicate.display.descriptor')
    static final private String ENTITY_DESC = message('inspection.entity.duplicate.display.descriptor')
    static final private LocalInspectionTool FORM_INSP = new DuplicatedFormInspection()
    static final private LocalInspectionTool SCREEN_INSP = new DuplicatedScreenInspection()
    static final private LocalInspectionTool MENU_INSP = new DuplicatedMenuInspection()
    static final private LocalInspectionTool SERVICE_INSP = new DuplicatedServiceXmlInspection()
    static final private LocalInspectionTool ENTITY_INSP = new DuplicatedEntityXmlInspection()

    static final private String DEST = 'zelda/widget'

    void testDuplicatedScreenInCurrentFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedScreenInCurrentCompoundFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedTargetScreen() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedTargetScreenInCompoundFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedFormInCurrentFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedFormInCurrentCompoundFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedTargetForm() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedTargetFormInCompoundFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedMenuInCurrentFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedMenuInCurrentCompoundFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedTargetMenu() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedTargetMenuInCompoundFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedServiceFromForm() { doTest(SERVICE_DESC, SERVICE_INSP) }

    void testDuplicatedEntityIsCalled() { doTest(ENTITY_DESC, ENTITY_INSP) }

    void testDuplicatedViewIsCalled() { doTest(ENTITY_DESC, ENTITY_INSP) }

    @Override
    protected String getLang() {
        return 'xml'
    }

    @Override
    protected void setUp() {
        super.setUp()
        String myTestFileName = "${this.getTestName(false)}.xml"
        myFixture.copyFileToProject("xml/$myTestFileName", "$DEST/$myTestFileName")
    }

    protected void doTest(boolean mustFind = true, String desc, LocalInspectionTool inspection) {
        myFixture.enableInspections(inspection)
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$DEST/$file")
        doHighlightTest(mustFind, desc)
    }

}
