package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class GroovyEntityFieldsFromDVECompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        GrMethodCall dveMethodCall = getParentOfType(element, GrMethodCall.class)
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
    static String getEntityNameFromDynamicView(GrMethodCall addAliasInitialMethod, PsiVariable initialDve, int index) {
        GrExpression[] params = addAliasInitialMethod.argumentList.expressionArguments
        if (params) {
            String aliasToLookFor = params[index].text
            List<UsageInfo> dveUsages = getUsagesOfVariable(initialDve)
            GrMethodCall relevantAddAlias = dveUsages.stream()
                    .map { UsageInfo usage ->
                        getParentOfType(usage.element, GrMethodCall.class)
                    }
                    .find { GrMethodCall addAliasCall ->
                        addAliasMethodUsesWantedAlias(addAliasCall, aliasToLookFor)
                    } as GrMethodCall
            if (!relevantAddAlias) return null
            return relevantAddAlias?.argumentList?.expressionArguments?[1]?.text

        }
        return null
    }

    static String getEntityNameFromDynamicView(GrMethodCall addAliasInitialMethod, PsiVariable initialDve) {
        return getEntityNameFromDynamicView(addAliasInitialMethod, initialDve, 0)
    }

    static boolean addAliasMethodUsesWantedAlias(GrMethodCall addAliasCall, String aliasToLookFor) {
        GrExpression[] params = addAliasCall.argumentList.expressionArguments
        String aliasParam = params?[0].text
        return aliasParam && aliasToLookFor == aliasParam
    }
}
