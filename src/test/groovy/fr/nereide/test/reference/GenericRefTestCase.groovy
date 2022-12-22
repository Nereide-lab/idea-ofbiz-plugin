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

package fr.nereide.test.reference

import com.intellij.lang.properties.references.PropertyReference
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.test.GenericOfbizPluginTestCase

class GenericRefTestCase extends GenericOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/reference"
    }

    protected void configureByFileAndTestRefTypeAndValue(String file, Class expectedRefType, String expectedRefValueName,
                                                         boolean strict) {
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            ref = ref.getReferences().find { expectedRefType.isAssignableFrom(it.getClass()) }
        }
        assert expectedRefType.isAssignableFrom(ref.getClass())
        if (strict) {
            assertEquals expectedRefValueName, ref.getElement().getName() ?
                    ref.getElement().getName() : getSafeTextInReference(ref)
                    as String
        } else {
            assert ref.getElement().getText().contains(expectedRefValueName)
        }
        assertNotNull "Reference for $expectedRefValueName not found", ref.resolve()
    }

    static String getSafeTextInReference(PsiReference ref) {
        ref.getElement().getText()
                .replaceAll('"', '')
                .replaceAll('\'', '')
    }

    protected void configureByFileAndTestRefTypeAndValue(String file, Class expectedRefType, String expectedRefValueName) {
        configureByFileAndTestRefTypeAndValue(file, expectedRefType, expectedRefValueName, true)
    }

    /**
     * Temporary workaround for tests to stay green
     */
    void testDummy() {
        assert true
    }
}
