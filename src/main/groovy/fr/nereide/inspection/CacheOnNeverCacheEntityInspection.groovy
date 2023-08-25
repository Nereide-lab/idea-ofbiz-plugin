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
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider
import fr.nereide.dom.EntityModelFile
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

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
            void visitIdentifier(@NotNull PsiIdentifier identifier) {
                // TODO : Surely there is a (cleaner) way to do this with java facade rather than psi text.
                if (!identifier.text.contains('cache')) return
                PsiMethodCallExpression query = PsiTreeUtil.getParentOfType(identifier, PsiMethodCallExpression)
                String entityName = EntityFieldCompletionProvider.getEntityNameFromDeclarationString(query.text)
                if (!entityName) return
                ProjectServiceInterface service = identifier.getProject().getService(ProjectServiceInterface.class)
                EntityModelFile.Entity entity = service.getEntity(entityName)
                if (!entity) return
                String neverCache = entity.getNeverCache()?:''
                if(!neverCache) return
                if(neverCache && neverCache == 'true') {
                    holder.registerProblem(
                            identifier,
                            InspectionBundle.message('inspection.entity.cache.on.never.cache.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myQuickFix
                    )
                }
            }
        }
    }
}
