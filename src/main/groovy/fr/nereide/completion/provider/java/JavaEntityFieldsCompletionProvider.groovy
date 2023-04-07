package fr.nereide.completion.provider.java


import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression fullCalledMethod = getParentOfType(element, PsiMethodCallExpression.class)

        if (fullCalledMethod.getText().startsWith('EntityQuery.use(')) { // on est dans une query
            return getEntityNameFromDeclarationString(fullCalledMethod.getText())
        }

        List<PsiReferenceExpression> fullGetStatementParts = getChildrenOfTypeAsList(fullCalledMethod,
                PsiReferenceExpression.class)

        List<PsiReferenceExpression> subGetStatementParts = getChildrenOfTypeAsList((fullGetStatementParts[0] as PsiElement),
                PsiReferenceExpression.class)

        PsiVariable gvVariable = subGetStatementParts[0].resolve() as PsiVariable
        PsiExpression init = gvVariable.getInitializer()
        if (init) {
            return getEntityNameFromDeclarationString(init.text)
        } else {
            PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement.class)
            if (basicFor) {
                PsiReferenceExpression iteratedValue = basicFor.getIteratedValue() as PsiReferenceExpression
                if (iteratedValue) {
                    PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
                    return getEntityNameFromDeclarationString(iteratedValueVariable.getInitializer().text)
                }
            }
        }
        return null
    }

}
