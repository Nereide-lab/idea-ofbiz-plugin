package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsFromDVEKeyMapCompletionProvider extends JavaEntityFieldsFromDVECompletionProvider {
    int index

    JavaEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression dveMethodCall = getParentOfType(
                getParentOfType(element, PsiMethodCallExpression.class),
                PsiMethodCallExpression.class)
        PsiVariable dveVariable = getPsiTopVariable(dveMethodCall)
        return dveVariable ? getEntityNameFromDynamicView(dveMethodCall, dveVariable, index) : null
    }
}
