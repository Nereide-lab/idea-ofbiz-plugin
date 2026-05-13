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
package fr.nereide.inspection.quickfix

import static com.intellij.psi.util.PsiTreeUtil.getChildOfType
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression

/**
 * Quickfix for removing the unnecessary cache call
 */
class RemoveCacheCallFix implements LocalQuickFix {

    final String name = InspectionBundle.message('inspection.entity.remove.cache.quickfix')

    final String familyName = name

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement cacheExpr = descriptor.psiElement

        Class methodClass = PsiMethodCallExpression
        Class refClass = PsiReferenceExpression

        if (MiscUtils.isGroovy(cacheExpr)) {
            methodClass = GrMethodCallExpression
            refClass = GrReferenceExpression
        }

        // codenarc-disable NoDef, VariableTypeRequired
        def methodCallWithCache = getParentOfType(cacheExpr, methodClass)
        def tempExpr = getChildOfType(methodCallWithCache, refClass)
        def methodCallWithoutCache = getChildOfType(tempExpr, methodClass)

        methodCallWithCache.replace(methodCallWithoutCache)
        // codenarc-enable NoDef, VariableTypeRequired
    }

}
