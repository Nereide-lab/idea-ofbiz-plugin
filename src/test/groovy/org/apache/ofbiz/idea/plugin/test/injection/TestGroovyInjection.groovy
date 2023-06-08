/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.test.injection

import org.jetbrains.plugins.groovy.GroovyLanguage

class TestGroovyInjection extends BaseInjectionTestCase {

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/injection/xml/groovy'
    }

    protected doTest() {
        super.doTest(GroovyLanguage.class)
    }

    /**
     * Todo : remove and reactivate test when there is a fix for these
     */
    void testWaitForFix() {
        assert true
    }

//    void testValueAttrInSetTagInScreen() {
//        doTest()
//    }
//
//    void testValueAttrInSetTagInMenu() {
//        doTest()
//    }
//
//    void testValueAttrInSetTagInForm() {
//        doTest()
//    }
//
//    void testUseWhenAttrInFieldTagInForm() {
//        doTest()
//    }
//
//    void testTooltipAttrInFieldTagInForm() {
//        doTest()
//    }
//
//    void testDefaultValueAttrInFieldTagInForm() {
//        doTest()
//    }
//
//    void testDefaultValueAttrInFieldTagInScreen() {
//        doTest()
//    }
//
//    void testLocationAttrInIncludeScreenTagInScreen() {
//        doTest()
//    }
//
//    void testDescriptionAttrInForm() {
//        doTest()
//    }
}