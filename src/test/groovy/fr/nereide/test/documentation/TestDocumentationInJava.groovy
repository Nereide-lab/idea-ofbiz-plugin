package fr.nereide.test.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

class TestDocumentationInJava extends GenericDocTestCase {

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
