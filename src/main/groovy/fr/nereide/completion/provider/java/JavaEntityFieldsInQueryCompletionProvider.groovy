package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement

/**
 * Entity fields completion provider for java in dve
 */
class JavaEntityFieldsInQueryCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromQuery(element, methodExprClass)
    }

}
