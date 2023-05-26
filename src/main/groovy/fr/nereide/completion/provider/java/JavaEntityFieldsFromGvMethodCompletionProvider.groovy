package fr.nereide.completion.provider.java


import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsFromGvMethodCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression getMethod = getParentOfType(element, PsiMethodCallExpression.class)

        PsiVariable gvVariable = getPsiTopVariable(getMethod)
        if (!gvVariable) return null
        PsiExpression gvInit = gvVariable.initializer
        if (gvInit) {
            // init instruction easily found, basic case
            return getEntityNameFromDeclarationString(gvInit.text)
        } else {
            // search for for loop
            PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement.class)
            if (basicFor) {
                return getEntityNameFromForStatement(basicFor)
                // search for query assignment
            } else {
                return getEntityNameFromLastQueryAssignment(gvVariable)
            }
        }
    }

}
