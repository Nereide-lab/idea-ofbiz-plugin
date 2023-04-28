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
        PsiVariable dveVariable
        if (isModelKeyMap(dveMethodCall)) {
            dveMethodCall = getParentOfType(dveMethodCall, PsiMethodCallExpression.class)
            dveVariable = getPsiTopVariable(dveMethodCall)
            getEntityNameFromDynamicView(dveMethodCall, dveVariable)
        } else {
            dveVariable = getPsiTopVariable(dveMethodCall)
            if (dveVariable.typeElement && dveVariable.typeElement.text == 'DynamicViewEntity') {
                return getEntityNameFromDynamicView(dveMethodCall, dveVariable)
            }
            return null
        }
    }

    /**
     * Checks if the method is from modelKeymap
     * @param originInitialMethod
     * @return
     */
    static boolean isModelKeyMap(PsiMethodCallExpression originInitialMethod) {
        return originInitialMethod ? originInitialMethod.methodExpression.qualifierExpression.text == 'ModelKeyMap' : false
    }

    /**
     * Tries to retrieve the entity name from the context Dynamic view
     * @param addAliasInitialMethod
     * @param initialDve
     * @return
     */
    static String getEntityNameFromDynamicView(PsiMethodCallExpression addAliasInitialMethod, PsiVariable initialDve) {
        PsiExpression[] params = addAliasInitialMethod.argumentList.expressions
        if (params) {
            String aliasToLookFor = params[0].text
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
    }

    static boolean addAliasMethodUsesWantedAlias(PsiMethodCallExpression addAliasCall, String aliasToLookFor) {
        PsiExpression[] params = addAliasCall.argumentList.expressions
        String aliasParam = params?[0].text
        return aliasParam && aliasToLookFor == aliasParam
    }
}
