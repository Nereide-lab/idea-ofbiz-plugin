package fr.nereide.completion.provider.groovy

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

/**
 * Part of the groovy entity fields completion system
 */
class GroovyEntityFieldsFromGvCompletionProvider extends GroovyEntityFieldCompletionProvider {

    String getEntityNameFromPsiElement(PsiElement element) {
        PsiVariable gvVariable
        if (isGroovySyntax(element)) {
            GrReferenceExpression genericValueRef = element.parent.firstChild as GrReferenceExpression
            gvVariable = genericValueRef.resolve() as PsiVariable
        } else {
            GrMethodCall getMethod = getParentOfType(element, GrMethodCall)
            gvVariable = getMethod?.firstChild?.firstChild?.resolve()
        }
        return retrieveEntityOrViewNameFromGrVariable(gvVariable) ?: null
    }

    private static boolean isGroovySyntax(PsiElement element) {
        return element.parent.firstChild instanceof GrReferenceExpression
    }

}
