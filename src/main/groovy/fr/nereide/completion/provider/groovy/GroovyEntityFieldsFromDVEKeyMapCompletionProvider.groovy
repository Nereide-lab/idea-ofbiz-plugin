package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement

class GroovyEntityFieldsFromDVEKeyMapCompletionProvider extends GroovyEntityFieldsFromDVECompletionProvider {
    int index

    GroovyEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromKeyMapInDve(element, getMethodExprClass(), this.index)
    }
}
