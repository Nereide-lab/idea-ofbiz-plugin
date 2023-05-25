package fr.nereide.completion.provider.java

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

abstract class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    /**
     * Get the entity name from a for statement
     * @param forStatement
     * @return
     */
    static String getEntityNameFromForStatement(PsiForeachStatement forStatement) {
        PsiReferenceExpression iteratedValue = forStatement.getIteratedValue() as PsiReferenceExpression
        if (iteratedValue) {
            PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
            return getEntityNameFromDeclarationString(iteratedValueVariable.getInitializer().text)
        }
        return null
    }

    Class getReferenceExpressionClass() {
        return PsiReferenceExpression.class
    }

    Class getAssigmentClass() {
        return PsiAssignmentExpression.class
    }

    String getAssigmentString(PsiElement assign) {
        return (assign as PsiAssignmentExpression).RExpression.text
    }
}
