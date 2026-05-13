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
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.inspection.common.InspectionUtil
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.GroovyLocalInspectionTool
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message
import static fr.nereide.project.utils.MiscUtils.isGroovy
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
            void visitReferenceExpression(GrReferenceExpression exp) {
                if (PluginActivator.getInstance(exp.project).inactive) return

                PsiMethod method
                Class methodCallClass = isGroovy(exp) ? GrMethodCall : PsiMethodCallExpression
                try {
                    if (!exp.resolve() || !exp.resolve() instanceof PsiMethod) {
                        return
                    }
                    method = exp.resolve() as PsiMethod
                } catch (ClassCastException ignored) {
                    return
                }

                if (!InspectionUtil.isCacheFromEntityQuery(method)) return
                if (InspectionUtil.cacheCallHasFalseParameter(exp)) return

                def call1 = PsiTreeUtil.getParentOfType(exp, methodCallClass)
                def call2 = PsiTreeUtil.getParentOfType(call1, methodCallClass)

                if (!isQueryCountFromEntityQuery(call2, methodCallClass)) {
                    return
                }

                PsiElement cachePsiEl = exp.lastChild
                holder.registerProblem(cachePsiEl,
                        message('inspection.entity.cache.on.count.display.descriptor'),
                        WARNING,
                        myQuickFix
                )
            }

        }
    }

    private static boolean isQueryCountFromEntityQuery(PsiAnnotationMemberValue call2, methodCallClass) {
        PsiElement[] candidates = PsiTreeUtil.collectElements(call2) { element ->
            if (!methodCallClass.isAssignableFrom(element.class)) return false
            return element?.invokedExpression?.referenceName == 'queryCount'
        }

        return candidates.any() { element ->
            GrReferenceExpression ref = element.getInvokedExpression() as GrReferenceExpression
            PsiMethod resolved = ref?.resolve() as PsiMethod
            resolved?.containingClass?.qualifiedName == OfbizPluginConstants.ENTITY_QUERY_CLASS
        }
    }
}
