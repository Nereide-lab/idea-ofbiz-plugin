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

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

@Ignore('Setup class, No tests here')
class BaseReferenceTestCase extends BaseOfbizPluginTestCase {

    static final String BASE_TEST_DIR = 'src/test/resources/testData/reference'

    /**
     * Default reference test file extention is xml
     */
    protected String getExtension() { return 'xml' }

    /**
     * Used for moving files in tests in case there is need for reference resolving
     */
    protected String getDestination() { return null }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
        if (getDestination()) {
            String file = "${this.getTestName(false)}.${getExtension()}"
            myFixture.moveFile(file, getDestination())
        }
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName) {
        doTest(expectedRefType, expectedRefValueName, true)
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName, boolean strict) {
        doTest(expectedRefType, expectedRefValueName, strict, false)
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName, boolean strict, boolean multiExpected) {
        String file = "${this.getTestName(false)}.${getExtension()}"
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            PsiMultiReference multiRef = ref
            ref = ref.getReferences().find { expectedRefType.isAssignableFrom(it.getClass()) }
            assertNoOtherRefType(multiRef, expectedRefType)
        }
        assert expectedRefType.isAssignableFrom(ref.getClass())
        if (strict) {
            assertEquals expectedRefValueName, ref.getValue() ?: getSafeTextInReference(ref) as String
        } else {
            assert ref.getElement().getText().contains(expectedRefValueName)
        }
        if (multiExpected) {
            ResolveResult[] resolves = (ref as PsiPolyVariantReference).multiResolve(false)
            assert "Multi reference for $expectedRefValueName not found", resolves.size() > 1
        } else {
            assertNotNull "Reference for $expectedRefValueName not found", ref.resolve()
        }
    }

    /**
     * Checks that no other reference type was found for an element.
     * For example, no screen reference resolved from a form
     * @param multiRef
     * @param expectedRefType
     */
    private static void assertNoOtherRefType(PsiMultiReference multiRef, Class expectedRefType) {
        assert (multiRef.getReferences() as List).stream()
                .filter { !expectedRefType.isAssignableFrom(it.getClass()) }
                .filter { it.getClass().getName().contains('fr.nereide') }
                .toList().size() == 0
    }

    static String getSafeTextInReference(PsiReference ref) {
        ref.getElement().getText()
                .replaceAll('"', '')
                .replaceAll('\'', '')
    }
}
