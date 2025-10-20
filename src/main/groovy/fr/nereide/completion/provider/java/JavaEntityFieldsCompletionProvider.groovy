package fr.nereide.completion.provider.java

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

/**
 * Entity fields completion provider for Java
 */
abstract class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    /**
     * Get the entity name from a for statement
     */
    static String getEntityNameFromForStatement(PsiForeachStatement forStatement) {
        PsiReferenceExpression iteratedValue = forStatement.iteratedValue as PsiReferenceExpression
        if (iteratedValue) {
            PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
            return getEntityNameFromDeclarationString(iteratedValueVariable.initializer.text)
        }
        return null
    }

    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

    final Class methodExprClass = PsiMethodCallExpression
    final Class referenceExpressionClass = PsiReferenceExpression
    final Class assigmentClass = PsiAssignmentExpression

    String getAssigmentString(PsiElement assign) {
        return (assign as PsiAssignmentExpression).RExpression.text
    }

    PsiElement[] getMethodArgs(PsiElement method) {
        return (method as PsiMethodCallExpression).argumentList.expressions
    }

}
