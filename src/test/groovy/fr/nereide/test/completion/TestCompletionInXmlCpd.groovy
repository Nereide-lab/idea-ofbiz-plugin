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
 * Completion tests for Xml, compound specific
 */
class TestCompletionInXmlCpd extends BaseCompletionTestCase {

    void testScreenCompletionInIncludeScreenInCpd() {
        doTest(['Kelvin'], true)
    }

    void testScreenCompletionInIncludeDistantScreenInCpd() {
        doTest(['GregsScreen', 'KelvinScreen'])
    }

    void testScreenCompletionInIncludeScreenInCpdFromController() {
        doTest(['HufftonScreen', 'VirginiaScreen'], true)
    }

    void testFormNameCompletionInCpdScreenWithLocationInCpd() {
        doTest(['AFormThatisCompletioned', 'AnOtherFormThatisCompletioned'], true)
    }

    void testFormNameCompletionInCpdFormWithLocationOutCpd() {
        doTest(['SomeDistantForestForm', 'SomeOtherDistantForestForm'])
    }

    void testSectionCompletionInSearchFindCompoundScreen() {
        doTest(['actions', 'menu-bar', 'single', 'content', 'list'])
    }

    private static final String MOVE_TO = 'forest-component/widget'

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/compound"
    }

    protected String getDestination() { return MOVE_TO }

}

