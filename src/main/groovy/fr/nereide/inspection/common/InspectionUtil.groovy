package fr.nereide.inspection.common

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.worker.EntityWorker
import org.jetbrains.plugins.groovy.lang.psi.api.statements.arguments.GrArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.completion.provider.common.EntityFieldCompletionProvider.getEntityNameFromDeclarationString
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_QUERY_CLASS
import static fr.nereide.project.utils.MiscUtils.isGroovy

class InspectionUtil {

    private static final Logger LOG = Logger.getInstance(InspectionUtil.class)

    /**
     * Checks the value of the parameter of the cache method.
     * Returns true as default so that the analysis doesn't continue
     * @param exp
     * @return true
     */
    static boolean cacheCallHasFalseParameter(PsiElement exp) {
        try {
            if (isGroovy(exp)) {
                GrArgumentList paramsListEl = PsiTreeUtil.getChildOfType(exp.getParent(), GrArgumentList.class)
                GrExpression[] cacheParams = paramsListEl.getExpressionArguments()
                if (!cacheParams) return
                GrLiteral cacheParam = cacheParams[0] as GrLiteral
                if (cacheParam && cacheParam.getValue() == Boolean.FALSE) {
                    return true
                }
                return false
            } else {
                PsiExpressionList[] paramsListEl = PsiTreeUtil.getChildrenOfType(exp.getParent(), PsiExpressionList.class)
                List<PsiExpression> cacheParams = paramsListEl[0].getExpressions()
                PsiLiteralExpression cacheParam = cacheParams[0] as PsiLiteralExpression
                if (!cacheParam) return false
                if (PsiTypes.booleanType() == cacheParam.getType() && cacheParam.getValue() == Boolean.FALSE) {
                    return true
                }
                return false
            }
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
        if (!entityQueryClass) return false
        return entityQueryClass.getMethods().contains(method) && method.getName() == 'cache'
    }

    static void checkAndRegisterCacheOnNeverCacheEntity(PsiElement exp, ProblemsHolder holder, RemoveCacheCallFix myQuickFix) {
        PsiMethod method
        Class methodCallClass = isGroovy(exp) ? GrMethodCall.class : PsiMethodCallExpression.class
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

    static boolean fileHasElementWithSameName(XmlFile file, String root, String namespace, XmlAttribute attr) {
        return fileHasElementWithSameName(file, root, namespace, attr.value)
    }

    static boolean fileHasElementWithSameName(XmlFile file, String root, String namespace, String attr) {
        List<XmlTag> subTags
        XmlTag rootTag = file.getRootTag()
        if (rootTag.getName() == 'compound-widgets') {
            subTags = rootTag.findSubTags(root, namespace)?[0].getSubTags()
        } else {
            subTags = rootTag.getSubTags()
        }
        return subTags.any { XmlTag tag -> tag.getAttribute('name') && tag.getAttribute('name').value == attr }
    }
}
