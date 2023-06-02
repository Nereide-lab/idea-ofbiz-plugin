package org.apache.ofbiz.idea.plugin.completion.provider.groovy

import com.intellij.psi.PsiElement

class GroovyEntityFieldsFromDVECompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromInsideDynamisView(element, getMethodExprClass())
    }
}
