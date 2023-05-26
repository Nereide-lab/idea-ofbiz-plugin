package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement

class JavaEntityFieldsInQueryCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromQuery(element, getMethodExprClass())
    }
}
