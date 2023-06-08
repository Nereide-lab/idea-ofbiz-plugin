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

package org.apache.ofbiz.idea.plugin.test.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

class TestDocumentationInJava extends BaseDocumentationTestCase {

    void testHoverDocOnServiceInJava() {
        myFixture.configureByFile('java/HoverDocOnServiceInJava.java')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("HelloWorld", PsiLiteralExpression.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnEntityInJava() {
        myFixture.configureByFile('java/HoverDocOnEntityInJava.java')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("Vi", PsiLiteralExpression.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnViewInJava() {
        myFixture.configureByFile('java/HoverDocOnViewInJava.java')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("RandomView", PsiLiteralExpression.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnPropertyInJava() {
        myFixture.configureByFile('java/HoverDocOnPropertyInJava.java')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText('TestPivooot', PsiLiteralExpression.class)
        assertHoverDocContains(element, originalElement, "Piv")
    }
}
