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

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue

/**
 * Documentation tests for xml
 */
// codenarc-disable DuplicateStringLiteral
class TestDocumentationInXml extends BaseDocumentationTestCase {

    /*********************
     * SERVICE DOC TESTS *
     *********************/

    void testQuickNavigateInfoOnServiceInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnServiceInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('HelloWorld', XmlAttributeValue)
        assertCorrectQuickNavigateInfo(element, originalElement)
    }

    void testHoverDocOnServiceInXml() {
        myFixture.configureByFile('xml/HoverDocOnServiceInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('HelloWorld', XmlAttributeValue)
        assertNotNullHoverDoc(element, originalElement)
    }

    void testFullDocOnServiceInXml() {
        myFixture.configureByFile('xml/FullDocOnServiceInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('HelloWorld', XmlAttributeValue)
        assertNotNullFullDoc(element, originalElement)
    }

    /********************
     * ENTITY DOC TESTS *
     ********************/

    void testQuickNavigateInfoOnEntityInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnEntityInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('Vi', XmlAttributeValue)
        assertCorrectQuickNavigateInfo(element, originalElement)
    }

    void testHoverDocOnEntityInXml() {
        myFixture.configureByFile('xml/HoverDocOnEntityInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('Vi', XmlAttributeValue)
        assertNotNullHoverDoc(element, originalElement)
    }

    /******************
     * VIEW DOC TESTS *
     ******************/

    void testQuickNavigateInfoOnViewInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnViewInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('RandomView', XmlAttributeValue)
        assertCorrectQuickNavigateInfo(element, originalElement)
    }

    void testHoverDocOnViewInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnViewInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('RandomView', XmlAttributeValue)
        assertNotNullHoverDoc(element, originalElement)
    }

    /**********************
     * PROPERTY DOC TESTS *
     **********************/

    /* codenarc-disable GStringExpressionWithinString */

    void testQuickNavigateInfoOnPropertyInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnPropertyInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('${uiLabelMap.TestPivooot}', XmlAttributeValue)
        assertCorrectQuickNavigateInfo(element, originalElement)
    }

    void testQuickNavigateInfoOnPropertyInNotSimpleTitleInXml() {
        myFixture.configureByFile('xml/QuickNavigateInfoOnPropertyInNotSimpleTitleInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture
                .findElementByText('${uiLabelMap.TestMyFooLabel}', XmlAttributeValue)
        assertCorrectQuickNavigateInfo(element, originalElement)
    }

    void testHoverDocOnPropertyInXml() {
        myFixture.configureByFile('xml/HoverDocOnPropertyInXml.xml')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('${uiLabelMap.TestPivooot}', XmlAttributeValue)
        assertHoverDocContains(element, originalElement, 'Piv')
    }
    /* codenarc-enable GStringExpressionWithinString */

}
