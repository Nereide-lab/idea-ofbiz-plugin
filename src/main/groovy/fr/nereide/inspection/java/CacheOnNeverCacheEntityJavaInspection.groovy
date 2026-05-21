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
package fr.nereide.inspection.java

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReferenceExpression
import fr.nereide.inspection.common.InspectionUtil
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import fr.nereide.project.PluginActivator
import org.jetbrains.annotations.NotNull

/**
 * Checks never cache call in java
 */
class CacheOnNeverCacheEntityJavaInspection extends OfbizBaseInspection {

    private final RemoveCacheCallFix myQuickFix = new RemoveCacheCallFix()

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            void visitReferenceExpression(PsiReferenceExpression cacheCallCandidate) {
                if (PluginActivator.getInstance(cacheCallCandidate.project).inactive) return
                InspectionUtil.checkAndRegisterCacheOnNeverCacheEntity(cacheCallCandidate, holder, myQuickFix)
            }

        }
    }

}
