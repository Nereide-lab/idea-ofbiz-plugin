package org.apache.ofbiz.idea.plugin.completion.provider.java

import com.intellij.psi.PsiElement

class JavaEntityFieldsFromDVECompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromInsideDynamisView(element, getMethodExprClass())
    }
}
