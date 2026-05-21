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

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiVariable
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.utils.PsiUtils

/**
 * Entity fields completion provider for java in dve
 */
class JavaEntityFieldsFromGvMethodCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression getMethod = getParentOfType(element, PsiMethodCallExpression)
        PsiVariable gvVariable = PsiUtils.getPsiTopVariable(getMethod, referenceExpressionClass)
        if (!gvVariable) return null
        PsiExpression gvInit = gvVariable.initializer
        if (gvInit) {
            // init instruction easily found, basic case
            return MiscUtils.getEntityNameFromDeclarationString(gvInit.text)
        }
        // search for for loop
        PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement)
        if (basicFor) {
            return getEntityNameFromForStatement(basicFor)
        }
        // search for query assignment
        return getEntityNameFromLastQueryAssignment(gvVariable)
    }

}
