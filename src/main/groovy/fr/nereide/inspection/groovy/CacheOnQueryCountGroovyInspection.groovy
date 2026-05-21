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
package fr.nereide.inspection.groovy

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.inspection.common.InspectionUtil
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.OfbizClassUtil
import fr.nereide.project.PluginActivator
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.GroovyLocalInspectionTool
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

/**
 * Groovy quickfix
 */
class CacheOnQueryCountGroovyInspection extends GroovyLocalInspectionTool {

    private final RemoveCacheCallFix myQuickFix = new RemoveCacheCallFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    GroovyElementVisitor buildGroovyVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new GroovyElementVisitor() {

            @Override
            void visitReferenceExpression(GrReferenceExpression cacheCallCandidate) {
                if (PluginActivator.getInstance(cacheCallCandidate.project).inactive) return

                PsiMethod cacheMethodCandidate
                try {
                    if (!cacheCallCandidate.resolve() || !cacheCallCandidate.resolve() instanceof PsiMethod) {
                        return
                    }
                    cacheMethodCandidate = cacheCallCandidate.resolve() as PsiMethod
                } catch (ClassCastException ignored) {
                    return
                }

                if (!InspectionUtil.isCacheFromEntityQuery(cacheMethodCandidate)) return
                if (InspectionUtil.cacheCallHasFalseParameter(cacheCallCandidate)) return

                GrMethodCall countCandidate1 = PsiTreeUtil.getParentOfType(cacheCallCandidate, GrMethodCall)
                if(!countCandidate1 || !isQueryCountFromEntityQuery(countCandidate1?.explicitCallReference?.resolve())) {
                    GrMethodCall countCandidate2 = PsiTreeUtil.getParentOfType(countCandidate1, GrMethodCall)
                    if(!countCandidate2 || !isQueryCountFromEntityQuery(countCandidate2?.explicitCallReference?.resolve())) {
                        return
                    }
                }

                PsiElement cachePsiEl = cacheCallCandidate.lastChild
                holder.registerProblem(cachePsiEl,
                        message('inspection.entity.cache.on.count.display.descriptor'),
                        WARNING,
                        myQuickFix
                )
            }
        }
    }

    static boolean isQueryCountFromEntityQuery(PsiElement countMethodCandidate) {
        return OfbizClassUtil.getEntityQueryClass(countMethodCandidate.project).getMethods()
                .findAll { method -> method.name == 'queryCount' }
                .any { method -> method == countMethodCandidate }
    }
}