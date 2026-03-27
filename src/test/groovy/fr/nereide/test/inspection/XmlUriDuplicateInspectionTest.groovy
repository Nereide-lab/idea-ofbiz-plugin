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
import fr.nereide.inspection.xml.DuplicatedUriInspection

/**
 * Inspection tests for duplicates in Xml, specifically Uri
 */
// codenarc-disable JUnitTestMethodWithoutAssert
class XmlUriDuplicateInspectionTest extends BaseInspectionTest {

    private static final String URI_DESC = message('inspection.uri.duplicate.display.descriptor')
    private static final LocalInspectionTool URI_INSP = new DuplicatedUriInspection()
    private static final String DEST = 'zelda/webapp/zelda'

    void testDuplicatedUriInCurrentController() { doTest(URI_DESC, URI_INSP) }

    void testDuplicatedUriInCurrentControllerWithDifferentMethod() { doTest(false, URI_DESC, URI_INSP) }

    private void doTest(boolean mustFind = true, String desc, LocalInspectionTool inspection) {
        myFixture.enableInspections(inspection)
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$DEST/$file")
        doHighlightTest(mustFind, desc)
    }

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

}
