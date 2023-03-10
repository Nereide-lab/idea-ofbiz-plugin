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

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizGroovyPatterns
import fr.nereide.project.worker.EntityWorker
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrForStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLoopStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.clauses.GrForInClause
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

import java.util.regex.Matcher
import java.util.regex.Pattern

class EntityFieldNameCompletionProvider extends CompletionProvider<CompletionParameters> {
    private static final Logger LOG = Logger.getInstance(EntityFieldNameCompletionProvider.class)
    private static final Pattern ENTITY_NAME_PATTERN = Pattern.compile("(['\"](.*?)['\"])")

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)

        PsiElement element = parameters.getPosition()
        Entity entity
        String entityName
        try {
            PsiElement genericValueRef = element.getParent().getFirstChild()
            assert genericValueRef instanceof GrReferenceExpression
            PsiElement initialVariable = genericValueRef.resolve()
            assert initialVariable instanceof GrVariable
            entityName = retrieveEntityOrViewNameFromGrVariable(initialVariable)
            if (!entityName) return
            entity = structureService.getEntity(entityName)
        } catch (Exception e) {
            LOG.error(e)
            return
        }
        if (entity) {
            List<String> entityFields = EntityWorker.getEntityFields(entity)
            entityFields.forEach { field ->
                result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(field), 1000))
            }
        } else {
            ViewEntity view = structureService.getViewEntity(entityName)
            List<String> viewFields = EntityWorker.getViewFields(view, structureService, 0)
            viewFields.forEach { field ->
                result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(field), 1000))
            }
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
            Matcher matcher = ENTITY_NAME_PATTERN.matcher(declarationString)
            String entityName = matcher.find() ? matcher.group(0) : null
            return entityName ? entityName.substring(1, entityName.length() - 1) : null
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
            } catch (ProcessCanceledException e) {
                LOG.info(e)
            } catch (Exception e) {
                LOG.warn(e)
            }
        }
    }

    private static String retrieveEntityOfViewNameFromOldFashionedLoop(GrLoopStatement oldFashionedLoop) {
        GrVariable iteratedList = null
        if (oldFashionedLoop instanceof GrForStatement) {
            def forDeclaration = (oldFashionedLoop as GrForStatement).getClause()
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
