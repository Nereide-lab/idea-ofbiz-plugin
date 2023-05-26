package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement

class JavaEntityFieldsFromDVEKeyMapCompletionProvider extends JavaEntityFieldsFromDVECompletionProvider {
    int index

    JavaEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromKeyMapInDve(element, getMethodExprClass(), this.index)
    }
}
