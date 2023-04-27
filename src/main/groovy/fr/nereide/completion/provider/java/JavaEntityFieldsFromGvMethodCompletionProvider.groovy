package fr.nereide.completion.provider.java

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import fr.nereide.project.pattern.OfbizPatternConst

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsFromGvMethodCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression getMethod = getParentOfType(element, PsiMethodCallExpression.class)

        PsiVariable gvVariable = getPsiTopVariable(getMethod)
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


    /**
     * Tries to extract the entity name from the context and potentials assignements
     * @param element
     * @param genericValueVariable
     * @return
     */
    static String getEntityNameFromLastQueryAssignment(PsiVariable genericValueVariable) {
        List<UsageInfo> usages = getUsagesOfVariable(genericValueVariable)
        if (!usages) return null
        LinkedList<UsageInfo> usagesInQuery = usages.stream().filter { usage ->
            PsiAssignmentExpression assign = getParentOfType(usage.element, PsiAssignmentExpression.class)
            assign && assign.RExpression.text.contains(OfbizPatternConst.QUERY_BEGINNING_STRING)
        }.toList()
        if (!usagesInQuery) return
        UsageInfo lastAssign = usagesInQuery.last
        PsiAssignmentExpression lastAssignExpr = getParentOfType(lastAssign.element, PsiAssignmentExpression.class)
        return getEntityNameFromDeclarationString(lastAssignExpr.RExpression.text)
    }
}
