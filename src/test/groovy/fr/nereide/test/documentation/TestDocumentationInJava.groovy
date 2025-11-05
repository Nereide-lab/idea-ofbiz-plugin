package fr.nereide.test.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

/**
 * Documentation tests for java
 */
class TestDocumentationInJava extends BaseDocumentationTestCase {

    void testHoverDocOnServiceInJava() {
        myFixture.configureByFile('java/HoverDocOnServiceInJava.java')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('HelloWorld', PsiLiteralExpression)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnEntityInJava() {
        myFixture.configureByFile('java/HoverDocOnEntityInJava.java')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('Vi', PsiLiteralExpression)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnViewInJava() {
        myFixture.configureByFile('java/HoverDocOnViewInJava.java')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('RandomView', PsiLiteralExpression)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnPropertyInJava() {
        myFixture.configureByFile('java/HoverDocOnPropertyInJava.java')
        final PsiElement element = myFixture.elementAtCaret
        final PsiElement originalElement = myFixture.findElementByText('TestPivooot', PsiLiteralExpression)
        assertHoverDocContains(element, originalElement, 'Piv')
    }

}
