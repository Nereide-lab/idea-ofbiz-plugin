package fr.nereide.completion.provider.java

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList

abstract class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    /**
     * Get the initial variable declaration
     * @param fullCalledMethod
     * @return
     */
    static PsiVariable getPsiTopVariable(PsiMethodCallExpression fullCalledMethod) {
        List fullGetStatementParts = getChildrenOfTypeAsList(fullCalledMethod, PsiReferenceExpression.class)
        List subGetStatementParts = getChildrenOfTypeAsList((fullGetStatementParts[0] as PsiElement), PsiReferenceExpression.class)
        return subGetStatementParts[0].resolve() as PsiVariable
    }

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

    Class getAssigmentClass() {
        return PsiAssignmentExpression.class
    }

    String getAssigmentString(PsiElement assign) {
        return (assign as PsiAssignmentExpression).RExpression.text
    }
}
