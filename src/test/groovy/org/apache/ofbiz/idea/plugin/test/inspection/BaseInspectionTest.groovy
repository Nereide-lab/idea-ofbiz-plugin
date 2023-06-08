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

package org.apache.ofbiz.idea.plugin.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import org.apache.ofbiz.idea.plugin.inspection.EmptyFileLocationInspection
import org.apache.ofbiz.idea.plugin.inspection.InspectionBundle
import org.apache.ofbiz.idea.plugin.test.BaseOfbizPluginTestCase

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    private static final LOCATION_QUICKFIX_NAME = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix')

    abstract String getLang()

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
        myFixture.enableInspections(new EmptyFileLocationInspection())
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/inspection"
    }

    protected void doTest() {
        myFixture.configureByFile("${getLang()}/${getTestName(false)}.${getLang()}")
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        assertFalse(highlightInfos.isEmpty())
        final IntentionAction action = myFixture.findSingleIntention(LOCATION_QUICKFIX_NAME)
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("${getLang()}/${getTestName(false)}.after.${getLang()}")
    }
}

