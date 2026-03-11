/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.project.utils

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList

import com.intellij.find.FindManager
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.find.impl.FindManagerImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import com.intellij.util.ArrayUtil
import com.intellij.util.CommonProcessors
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression

/**
 * Various methods for manipulation of PsiElements
 */
class PsiUtils {

    /**
     * Get the initial variable declaration
     */
    static PsiVariable getPsiTopVariable(PsiElement fullCalledMethod, Class aClass) {
        List fullGetStatementParts = getChildrenOfTypeAsList(fullCalledMethod, aClass)
        if (!fullGetStatementParts) return null
        List subGetStatementParts = getChildrenOfTypeAsList((fullGetStatementParts[0] as PsiElement), aClass)
        return subGetStatementParts ? subGetStatementParts[0].resolve() as PsiVariable : null
    }

    /**
     * returns the list of all usages of the variable
     * @param variable the psi variable that will be analyzed
     * @return the usages of the variable
     */
    static List<UsageInfo> getUsagesOfVariable(PsiVariable variable) {
        FindUsagesHandler handler = ((FindManagerImpl) FindManager.getInstance(variable.project))
                .findUsagesManager
                .getFindUsagesHandler(variable, false)
        if (!handler) return []
        // codenarc-disable ExplicitLinkedListInstantiation
        CommonProcessors.CollectProcessor<UsageInfo> processor =
                new CommonProcessors.CollectProcessor<>(Collections.synchronizedList(new LinkedList()))
        // codenarc-enable ExplicitLinkedListInstantiation
        if (!processor) return null[]
        List<PsiElement> psiElements = ArrayUtil.mergeArrays(handler.primaryElements, handler.secondaryElements)
        FindUsagesOptions options = handler.getFindUsagesOptions(null)
        for (PsiElement psiElement : psiElements) {
            handler.processElementUsages(psiElement, processor, options)
        }
        return processor.results
    }

    static List<GrAssignmentExpression> getGroovyVariableAssignements(PsiVariable variable) {
        List<UsageInfo> variableUsages = getUsagesOfVariable(variable)
        if (!variableUsages) return []
        return variableUsages
                .collect { ui -> ui.element.parent}
                .findAll { pe -> pe instanceof GrAssignmentExpression}
                .sort { a, b -> a.textOffset <=> b.textOffset }
    }

}
