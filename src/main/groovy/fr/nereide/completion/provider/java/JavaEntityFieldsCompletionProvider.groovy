package fr.nereide.completion.provider.java

import com.intellij.find.FindManager
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.find.impl.FindManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import com.intellij.util.ArrayUtil
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType
import static com.intellij.util.CommonProcessors.CollectProcessor

class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {


    public static final String QUERY_BEGINNING_STRING = 'EntityQuery.use('

    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression originMethod = getParentOfType(element, PsiMethodCallExpression.class)

        if (originMethod && originMethod.text.startsWith(QUERY_BEGINNING_STRING)) {
            return getEntityNameFromDeclarationString(originMethod.text)
        }
        PsiVariable initialTopVariable = getPsiTopVariable(originMethod)
        if (initialTopVariable.typeElement && initialTopVariable.typeElement.text == 'DynamicViewEntity') {
            return getEntityNameFromDynamicView(originMethod, initialTopVariable)
        }
        PsiExpression variableInit = initialTopVariable.initializer
        if (variableInit) {
            // init instruction easily found, basic case
            return getEntityNameFromDeclarationString(variableInit.text)
        } else {
            // search for for loop
            PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement.class)
            if (basicFor) {
                return getEntityNameFromForStatement(basicFor)
                // search for query assignment
            } else {
                return getEntityNameFromLastQueryAssignment(initialTopVariable)
            }
        }
    }

    static String getEntityNameFromDynamicView(PsiMethodCallExpression originMethod, PsiVariable initialTopVariable) {
        PsiExpression[] params = originMethod.argumentList.expressions
        if (params) {
            String aliasToLookFor = params[0].text
            List<UsageInfo> dveUsages = getUsagesOfVariable(initialTopVariable)
            PsiMethodCallExpression relevantAddAlias = dveUsages.stream()
                    .map { UsageInfo usage ->
                        getParentOfType(usage.element, PsiMethodCallExpression.class)
                    }
                    .find { PsiMethodCallExpression addAliasCall ->
                        aliasMethodUsesWantedAlias(addAliasCall, aliasToLookFor)
                    } as PsiMethodCallExpression
            if (!relevantAddAlias) return null
            return relevantAddAlias?.argumentList?.expressions?[1]?.text
        }
    }

    static boolean aliasMethodUsesWantedAlias(PsiMethodCallExpression addAliasCall, String aliasToLookFor) {
        PsiExpression[] params = addAliasCall.argumentList.expressions
        String aliasParam = params?[0].text
        return aliasParam && aliasToLookFor == aliasParam
    }

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
     * Tries to extract the entity name from the context and potentials assignements
     * @param element
     * @param genericValueTopVariable
     * @return
     */
    static String getEntityNameFromLastQueryAssignment(PsiVariable genericValueTopVariable) {
        List<UsageInfo> usages = getUsagesOfVariable(genericValueTopVariable)
        if (!usages) return null
        LinkedList<UsageInfo> usagesInQuery = usages.stream().filter { usage ->
            PsiAssignmentExpression assign = getParentOfType(usage.getElement(), PsiAssignmentExpression.class)
            assign && assign.getRExpression().getText().contains(QUERY_BEGINNING_STRING)
        }.toList()
        if (!usagesInQuery) return
        UsageInfo lastAssign = usagesInQuery.getLast()
        PsiAssignmentExpression lastAssignExpr = getParentOfType(lastAssign.getElement(), PsiAssignmentExpression.class)
        return getEntityNameFromDeclarationString(lastAssignExpr.getRExpression().getText())
    }

    /**
     * returns the list of all usages of the variable
     * @param variable
     * @return
     */
    static List<UsageInfo> getUsagesOfVariable(PsiVariable variable) {
        Project project = variable.getProject()
        FindUsagesHandler handler = ((FindManagerImpl) FindManager.getInstance(project))
                .getFindUsagesManager()
                .getFindUsagesHandler(variable, false)
        if (!handler) return null
        CollectProcessor<UsageInfo> processor = new CollectProcessor<>(Collections.synchronizedList(new ArrayList<>()))
        if (!processor) return null
        PsiElement[] psiElements = ArrayUtil.mergeArrays(handler.getPrimaryElements(), handler.getSecondaryElements())
        FindUsagesOptions options = handler.getFindUsagesOptions(null)
        for (PsiElement psiElement : psiElements) {
            handler.processElementUsages(psiElement, processor, options)
        }
        return processor.getResults()
    }

    /**
     * Get the entity name from a for statement
     * @param forStatement
     * @return
     */
    static String getEntityNameFromForStatement(PsiElement forStatement) {
        PsiReferenceExpression iteratedValue = forStatement.getIteratedValue() as PsiReferenceExpression
        if (iteratedValue) {
            PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
            return getEntityNameFromDeclarationString(iteratedValueVariable.getInitializer().text)
        }
        return null
    }

}
