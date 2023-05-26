package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.PsiVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.dataFlow.types.TypeInferenceHelper

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType
import static fr.nereide.project.pattern.OfbizPatternConst.DYNAMIC_VIEW_ENTITY_CLASS_NAME

class GroovyEntityFieldsFromDVECompletionProvider extends GroovyEntityFieldCompletionProvider {


    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        GrMethodCall dveMethodCall = getParentOfType(element, GrMethodCall.class)
        PsiVariable dveVariable = getPsiTopVariable(dveMethodCall)
        if (dveVariable && variableHasDveType(dveVariable, dveMethodCall)) {
            return getEntityNameFromDynamicView(dveMethodCall, dveVariable)
        }
        return null
    }

    static boolean variableHasDveType(PsiVariable dveVariable, GrMethodCall dveMethodCall) {
        if (dveVariable && dveVariable.typeElement) {
            return dveVariable.typeElement.text == DYNAMIC_VIEW_ENTITY_CLASS_NAME
        } else {
            GrReferenceExpression methodCallRMember = getChildrenOfType(dveMethodCall, GrReferenceExpression.class)[0]
            if (!methodCallRMember) return false
            GrReferenceExpression dveVarExpr = getChildrenOfType(methodCallRMember, GrReferenceExpression.class)[0]
            if (!dveVarExpr) return false
            PsiType type = TypeInferenceHelper.getInferredType(dveVarExpr)
            return type && type.presentableText == DYNAMIC_VIEW_ENTITY_CLASS_NAME
        }
    }

}
