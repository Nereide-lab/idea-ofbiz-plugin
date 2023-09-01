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

package fr.nereide.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.dom.EntityModelFile
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.completion.provider.common.EntityFieldCompletionProvider.getEntityNameFromDeclarationString
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_QUERY_CLASS

class CacheOnNeverCacheEntityInspection extends LocalInspectionTool {

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
                PsiMethodCallExpression query = PsiTreeUtil.getParentOfType(exp, PsiMethodCallExpression.class)
                String entityName = getEntityNameFromDeclarationString(query.text)
                if (!entityName) return

                ProjectServiceInterface service = exp.getProject().getService(ProjectServiceInterface.class)
                EntityModelFile.Entity entity = service.getEntity(entityName)
                if (!entity) return

                String neverCache = entity.getNeverCache() ?: ''
                if (!neverCache || neverCache != 'true') return

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
