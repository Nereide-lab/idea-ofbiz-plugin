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
package fr.nereide.test.completion

/**
 * Completion tests for xml that needs some files moving around
 */
// codenarc-disable DuplicateListLiteral, DuplicateStringLiteral
class TestCompletionInXmlWithMove extends BaseCompletionTestCase {

    void testTargetCompletionInForm() {
        doTest(['someTarget', 'some-other/target'], true)
    }

    void testTargetCompletionInScreenLink() {
        doTest(['someTarget', 'some-other/target'], true)
    }

    void testTargetCompletionInFormHyperlinkTarget() {
        doTest(['someTarget', 'some-other/target'], true)
    }

    void testTargetCompletionInMenuLink() {
        doTest(['someTarget', 'some-other/target'], true)
    }

    void testTargetCompletionInFormHyperlinkTargetWithInterAppLinkType() {
        doTest(['/zelda/control/zeldaWebappUri',
                '/link/control/linkWebappUri',
                '/pilgrim/control/someTarget',
                '/pilgrim/control/some-other/target'],
                true)
    }

    void testTargetCompletionInFormHyperLinkInCpd() {
        doTest(['someTarget', 'some-other/target', 'SomeRequestInCpd1', 'SomeRequestInCpd2'], true)
    }

    void testTargetCompletionInFormHyperLinkInterAppInCpd() {
        // TODO Ugly but couldn't find a better solution for the tests than insert a file during it...
        myFixture.copyFileToProject("misc/${getTestName(false)}.xml",
                'pilgrim/webapp/pilgrim/WEB-INF/testController.xml')
        doTest(['/zelda/control/zeldaWebappUri',
                '/link/control/linkWebappUri',
                '/pilgrim/control/someTarget',
                '/pilgrim/control/some-other/target',
                '/pilgrim/control/SomeRequestInCpd1',
                '/pilgrim/control/SomeRequestInCpd2'], true)
    }

    void testTargetCompletionInScreenLinkInCpd() {
        doTest(['someTarget', 'some-other/target', 'SomeRequestInCpd1a', 'SomeRequestInCpd2a'], true)
    }

    void testTargetCompletionInScreenLinkInterAppInCpd() {
        myFixture.copyFileToProject("misc/${getTestName(false)}.xml",
                'pilgrim/webapp/pilgrim/WEB-INF/testController.xml')
        doTest(['/zelda/control/zeldaWebappUri',
                '/link/control/linkWebappUri',
                '/pilgrim/control/someTarget',
                '/pilgrim/control/some-other/target',
                '/pilgrim/control/SomeRequestInCpd1c',
                '/pilgrim/control/SomeRequestInCpd2c'], true)
    }

    void testTargetCompletionInMenuLinkInCpd() {
        doTest(['someTarget', 'some-other/target', 'SomeRequestInCpd1d', 'SomeRequestInCpd2d'], true)
    }

    void testTargetCompletionInMenuLinkInterAppInCpd() {
        myFixture.copyFileToProject("misc/${getTestName(false)}.xml",
                'pilgrim/webapp/pilgrim/WEB-INF/testController.xml')
        doTest(['/zelda/control/zeldaWebappUri',
                '/link/control/linkWebappUri',
                '/pilgrim/control/someTarget',
                '/pilgrim/control/some-other/target',
                '/pilgrim/control/SomeRequestInCpd1d',
                '/pilgrim/control/SomeRequestInCpd2d'], true)
    }

    void testTargetCompletionInFormWithInclude() {
        myFixture.copyFileToProject("misc/${getTestName(false)}.xml",
                'zelda/webapp/zelda/WEB-INF/testController.xml')
        doTest(['someTarget',
                'some-other/target',
                'myIncludedRequest1',
                'myIncludedRequest2'], true)
    }

    private static final String MOVE_TO = 'pilgrim/widget'

    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/xml"
    }

    protected String getDestination() { return MOVE_TO }

}
