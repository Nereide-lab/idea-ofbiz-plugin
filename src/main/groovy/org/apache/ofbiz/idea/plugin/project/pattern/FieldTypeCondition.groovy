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

package org.apache.ofbiz.idea.plugin.project.pattern

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

class FieldTypeCondition extends PatternCondition<PsiElement> {
    String[] expectedType

    FieldTypeCondition(@Nullable String debugMethodName, String[] expectedType) {
        super(debugMethodName)
        this.expectedType = expectedType
    }

    @Override
    boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
        boolean isMatch = false
        if (element instanceof GrReferenceExpression) {
            PsiType myType = (element as GrReferenceExpression).getType()
            isMatch = myType && expectedType.contains(myType.getCanonicalText())
        }
        return isMatch
    }
}