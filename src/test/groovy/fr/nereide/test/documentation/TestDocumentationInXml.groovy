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

package fr.nereide.test.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

class TestDocumentationInXml extends GenericDocTestCase {

    void testDocumentationOnEntityInXml() {
        myFixture.configureByFile('xml/DocumentationOnEntityInXml.xml')

        // Targeted Element (Definition)
        final PsiElement element = myFixture.getElementAtCaret()
        // Actual element with caret on
        final PsiElement originalElement = myFixture.findElementByText("Vi", XmlAttribute.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element).getQuickNavigateInfo(element, originalElement)
        assertNotNull(generatedDoc)
        assertEquals('On peut faire ça à la dure ou… Attends, non, il n\'y a qu\'à la dure.', generatedDoc)
        assert true
    }

}
