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

package org.apache.ofbiz.idea.plugin.completion.provider.groovy

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class GroovyEntityFieldsFromGvCompletionProvider extends GroovyEntityFieldCompletionProvider {

    String getEntityNameFromPsiElement(PsiElement element) {
        PsiVariable gvVariable
        if (isGroovySyntax(element)) {
            GrReferenceExpression genericValueRef = element.parent.firstChild as GrReferenceExpression
            gvVariable = genericValueRef.resolve() as PsiVariable
        } else {
            GrMethodCall getMethod = getParentOfType(element, GrMethodCall.class)
            gvVariable = getMethod?.firstChild?.firstChild?.resolve()
        }
        return retrieveEntityOrViewNameFromGrVariable(gvVariable) ?: null
    }

    private static boolean isGroovySyntax(PsiElement element) {
        return element.parent.firstChild instanceof GrReferenceExpression
    }
}
