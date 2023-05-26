package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class GroovyEntityFieldsFromDVEKeyMapCompletionProvider extends GroovyEntityFieldsFromDVECompletionProvider {
    int index

    GroovyEntityFieldsFromDVEKeyMapCompletionProvider(int index) {
        this.index = index
    }

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        GrMethodCall dveMethodCall = getParentOfType(
                getParentOfType(element, GrMethodCall.class),
                GrMethodCallExpression.class)
        PsiVariable dveVariable = getPsiTopVariable(dveMethodCall)
        return dveVariable ? getEntityNameFromDynamicView(dveMethodCall, dveVariable, index) : null
    }
}
