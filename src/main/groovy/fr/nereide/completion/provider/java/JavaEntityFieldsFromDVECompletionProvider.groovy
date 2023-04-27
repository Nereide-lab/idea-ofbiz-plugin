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
        PsiMethodCallExpression originMethod = getParentOfType(element, PsiMethodCallExpression.class)
        PsiVariable initialTopVariable = getPsiTopVariable(originMethod)
        if (initialTopVariable.typeElement && initialTopVariable.typeElement.text == 'DynamicViewEntity') {
            return getEntityNameFromDynamicView(originMethod, initialTopVariable)
        }
        return null
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
}
