/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.inspection.common

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.completion.provider.common.EntityFieldCompletionProvider.getEntityNameFromDeclarationString
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.pattern.OfbizPluginConstants.ENTITY_QUERY_CLASS
import static fr.nereide.project.utils.MiscUtils.isGroovy

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionList
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiTypes
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

/**
 * Utility and mutualisation class for inspections
 */
class InspectionUtil {

    /**
     * Checks the value of the parameter of the cache method.
     * Returns true as default so that the analysis doesn't continue
     */
    static boolean cacheCallHasFalseParameter(PsiElement exp) {
        if (isGroovy(exp)) {
            GrArgumentList paramsListEl = PsiTreeUtil.getChildOfType(exp.parent, GrArgumentList)
            GrExpression[] cacheParams = paramsListEl.expressionArguments
            if (!cacheParams) return false
            GrLiteral cacheParam = cacheParams[0] as GrLiteral
            return (cacheParam && (cacheParam.value == Boolean.FALSE))
        }
        PsiExpressionList[] paramsListEl = PsiTreeUtil.getChildrenOfType(exp.parent, PsiExpressionList)
        List<PsiExpression> cacheParams = paramsListEl[0].expressions
        PsiLiteralExpression cacheParam = cacheParams[0] as PsiLiteralExpression
        if (!cacheParam) return false
        return ((PsiTypes.booleanType() == cacheParam.type) && (cacheParam.value == Boolean.FALSE))
    }

    /**
     * Checks if the cache call really is OFBiz's
     */
    static boolean isCacheFromEntityQuery(PsiMethod method) {
        PsiClass entityQueryClass = JavaPsiFacade.getInstance(method.project)
                .findClass(ENTITY_QUERY_CLASS, GlobalSearchScope.allScope(method.project))
        if (!entityQueryClass) return false
        return entityQueryClass.methods.contains(method) && method.name == 'cache'
    }

    static void checkAndRegisterCacheOnNeverCacheEntity(PsiElement exp, ProblemsHolder holder,
                                                        RemoveCacheCallFix myQuickFix) {
        PsiMethod method
        Class methodCallClass = isGroovy(exp) ? GrMethodCall : PsiMethodCallExpression
        try {
            if (!exp.resolve() || !exp.resolve() instanceof PsiMethod) { // codenarc-disable UnnecessaryInstanceOfCheck
                return
            }
            method = exp.resolve() as PsiMethod
        } catch (ClassCastException ignored) {
            return
        }

        if (!isCacheFromEntityQuery(method)) return
        if (cacheCallHasFalseParameter(exp)) return

        PsiAnnotationMemberValue query = PsiTreeUtil.getParentOfType(exp, methodCallClass)
        String entityName = getEntityNameFromDeclarationString(query.text)
        if (!entityName) return
        if (!EntityWorker.entityOrViewHasNeverCacheTrueAttr(entityName, exp.project)) return

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
        XmlTag rootTag = file.rootTag
        if (rootTag.name == 'compound-widgets') {
            subTags = rootTag.findSubTags(root, namespace)?[0].subTags
        } else {
            subTags = rootTag.subTags
        }
        return subTags.any { XmlTag tag ->
            XmlAttribute nameAttr = tag.getAttribute('name')
            nameAttr && nameAttr.value == attr
        }
    }

}
