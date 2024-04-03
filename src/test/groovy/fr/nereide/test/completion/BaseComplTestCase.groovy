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

package fr.nereide.test.completion

import com.intellij.codeInsight.completion.CompletionType
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

@Ignore('Parent class, No tests here')
class BaseComplTestCase extends BaseOfbizPluginTestCase {

    static final String BASE_TEST_DIR = 'src/test/resources/testData/completion'

    protected String getDestination() { return null }

    protected String getFileType() { return 'xml' }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/completion"
    }

    protected void doTest(List<String> expectedLookups, List<String> notExpectedLookups) {
        doTest(expectedLookups, notExpectedLookups, false)
    }

    protected void doTest(List<String> expectedLookups, boolean move) {
        doTest(expectedLookups, null, move)
    }

    protected void doTest(List<String> expectedLookups) {
        doTest(expectedLookups, null,)
    }

    protected void doTest(List<String> expectedLookups, List<String> notExpectedLookups, boolean move) {
        String file = "${this.getTestName(false)}.${getFileType()}"
        if (move && getDestination()) {
            myFixture.moveFile(file, getDestination())
            file = "${getDestination()}/$file"
        }
        myFixture.configureByFile(file)
        myFixture.complete(CompletionType.BASIC)
        List<String> lookupElementStrings = myFixture.getLookupElementStrings()
        if (expectedLookups) {
            assertContainsElements(lookupElementStrings, expectedLookups)
            if (notExpectedLookups) {
                assertDoesntContain(lookupElementStrings, notExpectedLookups)
            }
        } else {
            assert !lookupElementStrings
        }
    }
}
