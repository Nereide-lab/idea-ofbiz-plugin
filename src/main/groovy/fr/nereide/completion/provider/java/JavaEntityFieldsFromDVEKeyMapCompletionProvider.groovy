package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement

/**
 * Entity fields completion provider for java in dve
 */
class JavaEntityFieldsFromDVEKeyMapCompletionProvider extends JavaEntityFieldsFromDVECompletionProvider {

    int index

    JavaEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        return getEntityNameFromKeyMapInDve(element, methodExprClass, this.index)
    }

}
