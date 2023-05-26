package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement

class JavaEntityFieldsFromDVECompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromInsideDynamisView(element, getMethodExprClass())
    }
}
