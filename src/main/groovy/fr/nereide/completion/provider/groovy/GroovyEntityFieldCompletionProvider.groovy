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

package fr.nereide.completion.provider.groovy


import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrForStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLoopStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.clauses.GrForClause
import org.jetbrains.plugins.groovy.lang.psi.api.statements.clauses.GrForInClause
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

class GroovyEntityFieldCompletionProvider extends EntityFieldCompletionProvider {
    private static final Logger LOG = Logger.getInstance(GroovyEntityFieldCompletionProvider.class)

    String getEntityNameFromPsiElement(PsiElement element) {
        try {
            PsiElement genericValueRef = element.getParent().getFirstChild()
            assert genericValueRef instanceof GrReferenceExpression
            PsiElement initialVariable = genericValueRef.resolve()
            assert initialVariable instanceof GrVariable
            return retrieveEntityOrViewNameFromGrVariable(initialVariable) ?: null
        } catch (Exception ignored) {
            return null
        }
    }

    /**
     * Tries to get the entity or view name from the declaration of the Generic value or object
     * @param initialElement
     * @return the name or null if not found
     */
    private static String retrieveEntityOrViewNameFromGrVariable(GrVariable initialElement) {
        GrExpression declarationExpr = initialElement.getInitializerGroovy()
        String declarationString = declarationExpr ? declarationExpr.getText() : null
        if (declarationString) {
            getEntityNameFromDeclarationString(declarationString)
        } else {
            try {
                def oldFashionedLoop = PsiTreeUtil.getParentOfType(initialElement, GrLoopStatement.class)
                if (oldFashionedLoop) {
                    return retrieveEntityOfViewNameFromOldFashionedLoop(oldFashionedLoop)
                }
                GrReferenceExpression potentialLoop = getPotentialLoop(initialElement)
                if (potentialLoop && OfbizGroovyPatterns.GROOVY_LOOP_PATTERN.accepts(potentialLoop)) {
                    PsiElement gvList = getGVListVariablefromLoopInstruction(potentialLoop, 0)
                    assert gvList instanceof GrVariable
                    return retrieveEntityOrViewNameFromGrVariable(gvList)
                }
            } catch (Exception e) {
                LOG.warn(e)
            }
        }
    }

    private static String retrieveEntityOfViewNameFromOldFashionedLoop(GrLoopStatement oldFashionedLoop) {
        GrVariable iteratedList = null
        if (oldFashionedLoop instanceof GrForStatement) {
            GrForClause forDeclaration = (oldFashionedLoop as GrForStatement).getClause()
            if (forDeclaration instanceof GrForInClause) iteratedList = forDeclaration.getIteratedExpression().resolve()
        }
        return iteratedList ? retrieveEntityOrViewNameFromGrVariable(iteratedList) : null
    }

    private static PsiElement getGVListVariablefromLoopInstruction(GrReferenceExpression potentialLoop, int index) {
        if (index > 10) return null
        GrReferenceExpression expression = PsiTreeUtil.findChildOfType(potentialLoop, GrReferenceExpression.class, true)
        PsiElement gvList = expression.resolve()
        if (!gvList) { // on regarde au niveau du dessous
            return getGVListVariablefromLoopInstruction(expression, index++)
        }
        return gvList
    }

    private static GrReferenceExpression getPotentialLoop(GrVariable initialElement) {
        PsiElement bracketsBlock = PsiTreeUtil.getParentOfType(initialElement, GrClosableBlock.class)
        PsiElement fullCallBlock = PsiTreeUtil.getParentOfType(bracketsBlock, GrMethodCall.class)
        PsiElement potentialLoopCall = PsiTreeUtil.getChildOfType(fullCallBlock, GrReferenceExpression.class) ?: null
        return potentialLoopCall
    }
}
