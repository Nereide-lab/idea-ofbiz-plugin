package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement

/**
 * Part of the groovy entity fields completion system
 */
class GroovyEntityFieldsFromDVEKeyMapCompletionProvider extends GroovyEntityFieldsFromDVECompletionProvider {

    int index

    GroovyEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromKeyMapInDve(element, methodExprClass, this.index)
    }

}
