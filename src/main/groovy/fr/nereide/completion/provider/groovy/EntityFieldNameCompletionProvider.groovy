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
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomElement
import fr.nereide.dom.EntityModelFile.ViewEntityMember
import fr.nereide.dom.EntityModelFile.Alias
import fr.nereide.dom.EntityModelFile.AliasAll
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.EntityField
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
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
        try {
            PsiElement genericValueRef = element.getParent().getFirstChild()
            assert genericValueRef instanceof GrReferenceExpression

            PsiElement initialVariable = genericValueRef.resolve()
            assert initialVariable instanceof GrVariable

            String entityName = retrieveEntityOrViewName(initialVariable.getInitializerGroovy().getText())
            Entity entity = structureService.getEntity(entityName)

            if (entity) {
                generateLookupsWithEntity(entity, result)
            } else {
                ViewEntity view = structureService.getViewEntity(entityName)
                generateLookupsWithView(view, structureService, result, 0)
            }
        } catch (ProcessCanceledException e) {
            LOG.info(e)
        } catch (Exception e) {
            LOG.error(e)
        }
    }

    static void generateLookupsWithView(ViewEntity view, ProjectServiceInterface structureService, CompletionResultSet result,
                                        index) {
        if (index >= 10) return // infinite loop workaround
        List<Alias> aliases = view.getAliases()
        List<AliasAll> aliasAllList = view.getAliasAllList()
        if (aliasAllList) {
            List<ViewEntityMember> members = view.getMemberEntities()
            aliasAllList.each { aliasAllElmt ->
                String alias = aliasAllElmt.getEntityAlias()
                String entityName = members.find { it.getEntityAlias().getValue() == alias }?.getEntityName()
                if (entityName) {
                    Entity currentEntity = structureService.getEntity(entityName)
                    if (!currentEntity) {
                        ViewEntity currentView = structureService.getViewEntity(entityName)
                        generateLookupsWithView(currentView, structureService, result, index + 1)
                    } else {
                        generateLookupsWithEntity(currentEntity, result)
                    }
                }
            }
        }
        generateLookupElementsFromName(aliases, view, result)
    }

    private static void generateLookupsWithEntity(Entity entity, result) {
        List<EntityField> fields = entity.getFields()
        generateLookupElementsFromName(fields, entity, result)
    }

    private static generateLookupElementsFromName(List<DomElement> fields, DomElement entity, CompletionResultSet result) {
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName) createFieldLookupElement(entity.getEntityName().getRawText(), fieldName, result)
        }
    }

    private static String retrieveEntityOrViewName(String declarationExpr) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declarationExpr)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

    private static void createFieldLookupElement(String elementName, String fieldName, CompletionResultSet result) {
        LookupElement el = LookupElementBuilder.create(fieldName)
                .withTailText("(from entity ${elementName})", true)
        result.addElement(PrioritizedLookupElement.withPriority(el, 1000))
    }
}
