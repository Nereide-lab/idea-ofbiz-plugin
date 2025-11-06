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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.util.xml.DomManager
import fr.nereide.reference.common.EntityReference
import fr.nereide.reference.common.UiLabelReference
import fr.nereide.reference.xml.GroovyServiceDefReference
import fr.nereide.reference.xml.JavaMethodReference
import fr.nereide.reference.xml.RequestMapReference
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

/**
 * Base class for references tests
 */
@Ignore('Setup class, No tests here')
class BaseReferenceTestCase extends BaseOfbizPluginTestCase {

    protected static final String BASE_TEST_DIR = 'src/test/resources/testData/reference'

    protected String getExtension() { return 'xml' }

    protected String getDestination() { return null }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
        if (destination) {
            String file = "${this.getTestName(false)}.${extension}"
            myFixture.moveFile(file, destination)
        }
    }

    protected void doTest(boolean mustFind) {
        doTest(null, null, false, mustFind)
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName) {
        doTest(expectedRefType, expectedRefValueName, false, true)
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName, boolean multiExpected) {
        doTest(expectedRefType, expectedRefValueName, multiExpected, true)
    }

    protected void doTest(Class expectedRefType, String expectedRefValueName, boolean multiExpected, boolean mustFind) {
        String file = "${this.getTestName(false)}.${extension}"
        myFixture.configureByFile(file)
        /* codenarc-disable UnnecessaryGetter */
        PsiReference myRef = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (!mustFind) {
            assert !myRef.resolve()
            return
        }
        /* codenarc-enable UnnecessaryGetter */
        if (myRef instanceof PsiMultiReference) {
            PsiMultiReference multiRef = myRef
            myRef = myRef.references.find { ref -> expectedRefType.isAssignableFrom(ref.class) }
            assertNoOtherRefType(multiRef, expectedRefType)
        }
        assert expectedRefType.isAssignableFrom(myRef.class)
        PsiElement resolve
        if (multiExpected) {
            ResolveResult[] resolves = (myRef as PsiPolyVariantReference).multiResolve(false)
            assert "Multi reference for $expectedRefValueName not found", resolves.size() > 1
            resolve = resolves[0].element
        } else {
            resolve = myRef.resolve()
            assert resolve
        }
        DomManager dm = DomManager.getDomManager(myFixture.project)
        String refValueName
        switch (myRef) {
            case FileReference:
                String fileName = (myRef as FileReference).resolve().name
                refValueName = fileName.substring(0, fileName.indexOf('.'))
                break
            case JavaMethodReference:
            case GroovyServiceDefReference:
                refValueName = (resolve as PsiMethod).name
                break
            case UiLabelReference:
                refValueName = dm.getDomElement(resolve).key
                break
            case EntityReference:
                refValueName = dm.getDomElement(resolve).entityName
                break
            case RequestMapReference:
                refValueName = dm.getDomElement(resolve).uri
                break
            default:
                refValueName = dm.getDomElement(resolve).name
        }
        assert refValueName == expectedRefValueName
    }

    /**
     * Checks that no other reference type was found for an element.
     * For example, no screen reference resolved from a form
     */
    private static void assertNoOtherRefType(PsiMultiReference multiRef, Class expectedRefType) {
        assert (multiRef.references as List).stream()
                .filter { ref -> !expectedRefType.isAssignableFrom(ref.class) }
                .filter { ref -> ref.class.name.contains('fr.nereide') }
                .toList().size() == 0
    }

}
