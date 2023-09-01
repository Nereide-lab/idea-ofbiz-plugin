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

package fr.nereide.inspection.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiReferenceParameterList
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.inspection.InspectionBundle
import org.jetbrains.annotations.NotNull

class RemoveCacheCallFix implements LocalQuickFix {

    @Override
    String getName() {
        return InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
    }

    @Override
    String getFamilyName() {
        return getName()
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiIdentifier cacheExpr = descriptor.getPsiElement() as PsiIdentifier

        // delete before ('.' carac and optional empty list)
        PsiElement prevEl = PsiTreeUtil.prevLeaf(cacheExpr, false)
        PsiElement elToDelete
        while (prevEl && ((prevEl instanceof PsiReferenceParameterList) || (prevEl.text == '.'))) {
            elToDelete = prevEl
            prevEl = PsiTreeUtil.prevLeaf(prevEl)
            elToDelete.delete()
        }
        // delete parenthesis after
        PsiElement elAfter = PsiTreeUtil.nextLeaf(cacheExpr).getParent()
        elAfter.delete()

        // delete cache expr
        cacheExpr.delete()
    }
}
