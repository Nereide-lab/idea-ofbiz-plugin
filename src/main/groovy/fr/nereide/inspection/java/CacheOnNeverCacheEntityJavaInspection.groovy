/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.nereide.inspection.java

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.worker.EntityWorker
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.completion.provider.common.EntityFieldCompletionProvider.getEntityNameFromDeclarationString
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_QUERY_CLASS

class CacheOnNeverCacheEntityJavaInspection extends LocalInspectionTool {

    private final RemoveCacheCallFix myQuickFix = new RemoveCacheCallFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            void visitReferenceExpression(PsiReferenceExpression exp) {
                PsiMethod method
                try {
                    if (!exp.resolve() || !exp.resolve() instanceof PsiMethod) return
                    method = exp.resolve() as PsiMethod
                } catch (ClassCastException ignored) {
                    return
                }

                if (!isCacheFromEntityQuery(method)) return
                if (cacheCallHasFalseParameter(exp)) return

                PsiMethodCallExpression query = PsiTreeUtil.getParentOfType(exp, PsiMethodCallExpression.class)
                String entityName = getEntityNameFromDeclarationString(query.text)
                if (!entityName) return
                if (!EntityWorker.entityOrViewHasNeverCacheTrueAttr(entityName, exp.getProject())) return

                PsiIdentifier cachePsiEl = exp.lastChild as PsiIdentifier
                holder.registerProblem(cachePsiEl,
                        message('inspection.entity.cache.on.never.cache.display.descriptor'),
                        WARNING,
                        myQuickFix
                )
            }
        }
    }

    /**
     * Checks the value of the parameter of the cache method.
     * Returns true as default so that the analysis doesn't continue
     * @param exp
     * @return true
     */
    static boolean cacheCallHasFalseParameter(PsiReferenceExpression exp) {
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
}
