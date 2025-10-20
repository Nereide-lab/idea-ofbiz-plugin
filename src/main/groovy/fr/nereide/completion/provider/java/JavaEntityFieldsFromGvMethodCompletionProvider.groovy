package fr.nereide.completion.provider.java

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable

/**
 * Entity fields completion provider for java in dve
 */
class JavaEntityFieldsFromGvMethodCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression getMethod = getParentOfType(element, PsiMethodCallExpression)
        PsiVariable gvVariable = getPsiTopVariable(getMethod)
        if (!gvVariable) return null
        PsiExpression gvInit = gvVariable.initializer
        if (gvInit) {
            // init instruction easily found, basic case
            return getEntityNameFromDeclarationString(gvInit.text)
        }
        // search for for loop
        PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement)
        if (basicFor) {
            return getEntityNameFromForStatement(basicFor)
        }
        // search for query assignment
        return getEntityNameFromLastQueryAssignment(gvVariable)
    }

}
