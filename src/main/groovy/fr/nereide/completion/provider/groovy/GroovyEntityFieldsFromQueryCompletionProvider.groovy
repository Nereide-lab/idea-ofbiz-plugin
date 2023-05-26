package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement

class GroovyEntityFieldsFromQueryCompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromQuery(element, getMethodExprClass())
    }
}
