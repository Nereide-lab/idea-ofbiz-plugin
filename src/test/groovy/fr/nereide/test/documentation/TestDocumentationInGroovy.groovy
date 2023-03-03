package fr.nereide.test.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

class TestDocumentationInGroovy extends BaseDocumentationTestCase {

    void testHoverDocOnServiceRunCallInGroovy() {
        myFixture.configureByFile('groovy/HoverDocOnServiceRunCallInGroovy.groovy')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("HelloWorld", GrLiteral.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnServiceRunSyncCallInGroovy() {
        myFixture.configureByFile('groovy/HoverDocOnServiceRunSyncCallInGroovy.groovy')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("HelloWorld", GrLiteral.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnEntityInGroovy() {
        myFixture.configureByFile('groovy/HoverDocOnEntityInGroovy.groovy')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("Vi", GrLiteral.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }

    void testHoverDocOnViewInGroovy() {
        myFixture.configureByFile('groovy/HoverDocOnViewInGroovy.groovy')
        final PsiElement element = myFixture.getElementAtCaret()
        final PsiElement originalElement = myFixture.findElementByText("RandomView", GrLiteral.class)

        final String generatedDoc = DocumentationManager.getProviderFromElement(element)
                .generateHoverDoc(element, originalElement)
        assertNotNull(generatedDoc)
    }
}
