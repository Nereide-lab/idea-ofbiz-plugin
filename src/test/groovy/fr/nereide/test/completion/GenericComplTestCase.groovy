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
import fr.nereide.test.GenericOfbizPluginTestCase

class GenericComplTestCase extends GenericOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/completion"
    }

    protected List<String> configureByFileAndGetLookupsElements(String file) {
        myFixture.configureByFile(file)
        myFixture.complete(CompletionType.BASIC)
        return myFixture.getLookupElementStrings()
    }

    /**
     * Temporary workaround for tests to stay green
     */
    void testDummy() {
        assert true
    }
}
