package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsFromDVECompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression dveMethodCall = getParentOfType(element, PsiMethodCallExpression.class)
        PsiVariable dveVariable = getPsiTopVariable(dveMethodCall)
        if (dveVariable && dveVariable.typeElement && dveVariable.typeElement.text == 'DynamicViewEntity') {
            return getEntityNameFromDynamicView(dveMethodCall, dveVariable)
        }
        return null
    }

}
