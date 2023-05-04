package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsFromDVECompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression dveMethodCall = getParentOfType(element, PsiMethodCallExpression.class)
        PsiVariable dveVariable = getPsiTopVariable(dveMethodCall)
        if (dveVariable && dveVariable.typeElement && dveVariable.typeElement.text == 'DynamicViewEntity') {
            return getEntityNameFromDynamicView(dveMethodCall, dveVariable)
        }
        return null
    }

    /**
     * Tries to retrieve the entity name from the context Dynamic view
     * @param addAliasInitialMethod addAlias method where the completion takes place
     * @param initialDve dve java variable
     * @param index index of the alias maram to use for entityName
     * @return
     */
    static String getEntityNameFromDynamicView(PsiMethodCallExpression addAliasInitialMethod, PsiVariable initialDve, int index) {
        PsiExpression[] params = addAliasInitialMethod.argumentList.expressions
        if (params) {
            String aliasToLookFor = params[index].text
            List<UsageInfo> dveUsages = getUsagesOfVariable(initialDve)
            PsiMethodCallExpression relevantAddAlias = dveUsages.stream()
                    .map { UsageInfo usage ->
                        getParentOfType(usage.element, PsiMethodCallExpression.class)
                    }
                    .find { PsiMethodCallExpression addAliasCall ->
                        addAliasMethodUsesWantedAlias(addAliasCall, aliasToLookFor)
                    } as PsiMethodCallExpression
            if (!relevantAddAlias) return null
            return relevantAddAlias?.argumentList?.expressions?[1]?.text
        }
        return null
    }

    static String getEntityNameFromDynamicView(PsiMethodCallExpression addAliasInitialMethod, PsiVariable initialDve) {
        return getEntityNameFromDynamicView(addAliasInitialMethod, initialDve, 0)
    }

    static boolean addAliasMethodUsesWantedAlias(PsiMethodCallExpression addAliasCall, String aliasToLookFor) {
        PsiExpression[] params = addAliasCall.argumentList.expressions
        String aliasParam = params?[0].text
        return aliasParam && aliasToLookFor == aliasParam
    }
}
