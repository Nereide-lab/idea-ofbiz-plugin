package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement

/**
 * Part of the groovy entity fields completion system
 */
class GroovyEntityFieldsFromDVECompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromInsideDynamisView(element, methodExprClass)
    }

}
