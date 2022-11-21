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
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
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

    /**
     * Workaround for groovy refs, which brings back a multi ref object.
     * We don't want that
     * @param testFolder
     * @param type
     * @return
     */
    PsiReference setupFixtureForTestAndGetRefForGroovy(String file) {
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        if (ref instanceof PsiMultiReference) {
            def multi = ref as PsiMultiReference
            for (PsiReference curRef : multi.getReferences()) {
                if (!(curRef instanceof PropertyReference)) {
                    return curRef
                }
            }
        }
        return ref
    }

    protected void configureByFileAndTestRefTypeAndValueForXml(String file, Class expectedRefType, String expectedRefValueName,
                                                               boolean strict) {
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            ref = ref.getReferences().find { expectedRefType.isAssignableFrom(it.getClass()) }
        }
        assert expectedRefType.isAssignableFrom(ref.getClass())
        if (strict) {
            assertEquals expectedRefValueName, ref.getElement().getName() as String
        } else {
            assert ref.getElement().getText().contains(expectedRefValueName)
        }
        assertNotNull "Reference for $expectedRefValueName not found", ref.resolve()
    }

    protected void configureByFileAndTestRefTypeAndValueForXml(String file, Class expectedRefType, String expectedRefValueName) {
        configureByFileAndTestRefTypeAndValueForXml(file, expectedRefType, expectedRefValueName, true)
    }

    /**
     * Temporary workaround for tests to stay green
     */
    void testDummy() {
        assert true
    }
}
