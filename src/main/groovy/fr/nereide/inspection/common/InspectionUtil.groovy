package fr.nereide.inspection.common

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.worker.EntityWorker
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.completion.provider.common.EntityFieldCompletionProvider.getEntityNameFromDeclarationString
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_QUERY_CLASS

class InspectionUtil {


    /**
     * Checks the value of the parameter of the cache method.
     * Returns true as default so that the analysis doesn't continue
     * @param exp
     * @return true
     */
    static boolean cacheCallHasFalseParameter(PsiElement exp) {
        try {
            PsiExpressionList[] paramsListEl = PsiTreeUtil.getChildrenOfType(exp.getParent(), PsiExpressionList.class)
            List<PsiExpression> cacheParams = paramsListEl[0].getExpressions()
            PsiLiteralExpression cacheParam = cacheParams[0] as PsiLiteralExpression
            if (!cacheParam) return false
            if (PsiTypes.booleanType() == cacheParam.getType() && cacheParam.getValue() == Boolean.FALSE) {
                return true
            }
            return false
        } catch (Exception ignored) {
            return true
        }
    }

    /**
     * Checks if the cache call really is OFBiz's
     * @param method
     * @return
     */
    static boolean isCacheFromEntityQuery(PsiMethod method) {
        PsiClass entityQueryClass = JavaPsiFacade.getInstance(method.getProject())
                .findClass(ENTITY_QUERY_CLASS, GlobalSearchScope.allScope(method.getProject()))
        return entityQueryClass.getMethods().contains(method) && method.getName() == 'cache'
    }


    static void checkAndRegisterCacheOnNeverCacheEntity(PsiElement exp, ProblemsHolder holder, RemoveCacheCallFix myQuickFix) {
        PsiMethod method
        Class methodCallClass = MiscUtils.isGroovy(exp) ? GrMethodCall.class : PsiMethodCallExpression.class
        try {
            if (!exp.resolve() || !exp.resolve() instanceof PsiMethod) return
            method = exp.resolve() as PsiMethod
        } catch (ClassCastException ignored) {
            return
        }

        if (!isCacheFromEntityQuery(method)) return
        if (cacheCallHasFalseParameter(exp)) return

        def query = PsiTreeUtil.getParentOfType(exp, methodCallClass)
        String entityName = getEntityNameFromDeclarationString(query.text)
        if (!entityName) return
        if (!EntityWorker.entityOrViewHasNeverCacheTrueAttr(entityName, exp.getProject())) return

        PsiElement cachePsiEl = exp.lastChild
        holder.registerProblem(cachePsiEl,
                message('inspection.entity.cache.on.never.cache.display.descriptor'),
                WARNING,
                myQuickFix
        )
    }
}
