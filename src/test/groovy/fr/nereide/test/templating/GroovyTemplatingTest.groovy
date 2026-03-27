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
package fr.nereide.test.templating

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.codeInsight.template.impl.actions.ListTemplatesAction
import fr.nereide.test.BaseOfbizPluginTestCase

/**
 * Heavily inspired from {@code com.intellij.java.codeInsight.template.LiveTemplateTestCase}
 * and  {@code org.jetbrains.plugins.groovy.lang.GroovyLiveTemplatesTest }
 */
class GroovyTemplatingTest extends BaseOfbizPluginTestCase {

    void testGroovyServiceSimpleExpandInGroovyScript() {
        String file = "${this.getTestName(false)}.groovy"
        myFixture.configureByFile(file)
        new ListTemplatesAction().actionPerformedImpl(myFixture.editor.project, myFixture.editor)
        (LookupManager.getActiveLookup(myFixture.editor) as LookupImpl).finishLookup(Lookup.NORMAL_SELECT_CHAR)
        assertSameLinesWithFile(expectedFileLocation, myFixture.file.text)
    }

    private String getExpectedFileLocation() {
        return testDataPath + '/reference/' + getTestName(false) + '.groovy.expected'
    }

    @Override
    protected void setUp() {
        super.setUp()
        TemplateManagerImpl.templateTesting = myFixture.testRootDisposable
    }

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/templating'
    }

}
