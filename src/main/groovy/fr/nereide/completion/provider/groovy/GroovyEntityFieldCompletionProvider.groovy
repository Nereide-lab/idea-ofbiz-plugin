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
package fr.nereide.completion.provider.groovy

import fr.nereide.project.utils.MiscUtils

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType
import static com.intellij.psi.util.PsiTreeUtil.getChildOfType
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrForStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLoopStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.clauses.GrForClause
import org.jetbrains.plugins.groovy.lang.psi.api.statements.clauses.GrForInClause
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

/**
 * Entity fields completion provider for Groovy lang.
 */
abstract class GroovyEntityFieldCompletionProvider extends EntityFieldCompletionProvider {

    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

    final Class referenceExpressionClass = GrReferenceExpression
    final Class assigmentClass = GrAssignmentExpression
    final Class methodExprClass = GrMethodCall

    String getAssigmentString(PsiElement assign) {
        return (assign as GrAssignmentExpression).RValue.text
    }

    PsiElement[] getMethodArgs(PsiElement method) {
        return (method as GrMethodCall).argumentList.expressionArguments
    }

    protected static PsiElement getGVListVariablefromLoopInstruction(GrReferenceExpression potentialLoop, int index) {
        if (index > 10) return null
        GrReferenceExpression expression = findChildOfType(potentialLoop, GrReferenceExpression, true)
        PsiElement gvList = expression.resolve()
        if (!gvList || gvList instanceof PsiMethod) { // on regarde au niveau du dessous
            return getGVListVariablefromLoopInstruction(expression, index++)
        }
        return gvList
    }

    protected static GrReferenceExpression getPotentialLoop(GrVariable initialElement) {
        PsiElement bracketsBlock = getParentOfType(initialElement, GrClosableBlock)
        PsiElement fullCallBlock = getParentOfType(bracketsBlock, GrMethodCall)
        PsiElement potentialLoopCall = getChildOfType(fullCallBlock, GrReferenceExpression) ?: null
        return potentialLoopCall
    }

    /**
     * Tries to get the entity or view name from the declaration of the Generic value or object
     */
    protected String retrieveEntityOrViewNameFromGrVariable(PsiVariable initialElement) {
        PsiExpression init = initialElement.initializer
        String declarationString = init ? init.text : initialElement.initializerGroovy?.text
        if (declarationString && !(declarationString == 'null')) {
            return MiscUtils.getEntityNameFromDeclarationString(declarationString)
        }
        GrLoopStatement oldFashionedLoop = getParentOfType(initialElement, GrLoopStatement)
        if (oldFashionedLoop) {
            return retrieveEntityOfViewNameFromOldFashionedLoop(oldFashionedLoop)
        }
        GrReferenceExpression potentialLoop = getPotentialLoop(initialElement)
        if (potentialLoop && OfbizGroovyPatterns.GROOVY_LOOP_PATTERN.accepts(potentialLoop)) {
            PsiElement gvList = getGVListVariablefromLoopInstruction(potentialLoop, 0)
            assert gvList instanceof GrVariable
            return retrieveEntityOrViewNameFromGrVariable(gvList)
        }
        return getEntityNameFromLastQueryAssignment(initialElement)
    }

    protected String retrieveEntityOfViewNameFromOldFashionedLoop(GrLoopStatement oldFashionedLoop) {
        GrVariable iteratedList = null
        if (oldFashionedLoop instanceof GrForStatement) {
            GrForClause forDeclaration = (oldFashionedLoop as GrForStatement).clause
            if (forDeclaration instanceof GrForInClause) iteratedList = forDeclaration.iteratedExpression.resolve()
        }
        return iteratedList ? retrieveEntityOrViewNameFromGrVariable(iteratedList) : null
    }

}
