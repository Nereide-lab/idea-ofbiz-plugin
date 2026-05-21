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
package fr.nereide.completion.provider.java

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider
import fr.nereide.project.utils.MiscUtils

/**
 * Entity fields completion provider for Java
 */
abstract class JavaEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    /**
     * Get the entity name from a for statement
     */
    static String getEntityNameFromForStatement(PsiForeachStatement forStatement) {
        PsiReferenceExpression iteratedValue = forStatement.iteratedValue as PsiReferenceExpression
        if (iteratedValue) {
            PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
            return MiscUtils.getEntityNameFromDeclarationString(iteratedValueVariable.initializer.text)
        }
        return null
    }

    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

    final Class methodExprClass = PsiMethodCallExpression
    final Class referenceExpressionClass = PsiReferenceExpression
    final Class assigmentClass = PsiAssignmentExpression

    String getAssigmentString(PsiElement assign) {
        return (assign as PsiAssignmentExpression).RExpression.text
    }

    PsiElement[] getMethodArgs(PsiElement method) {
        return (method as PsiMethodCallExpression).argumentList.expressions
    }

}
