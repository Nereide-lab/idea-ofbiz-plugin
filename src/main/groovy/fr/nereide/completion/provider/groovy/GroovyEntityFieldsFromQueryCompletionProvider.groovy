package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement

/**
 * Part of the groovy entity fields completion system
 */
class GroovyEntityFieldsFromQueryCompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromQuery(element, methodExprClass)
    }

}
