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

    static void generateLookupsWithView(ViewEntity view, ProjectServiceInterface structureService, CompletionResultSet result, index) {
        generateLookupsWithView(view, null, structureService, [], result, index)
    }

    static void generateLookupsWithView(ViewEntity view, String prefix, ProjectServiceInterface structureService, List<String> excludedFields,
                                        CompletionResultSet result, index) {
        if (index >= 10) return // infinite loop workaround
        List<Alias> aliases = view.getAliases()
        List<AliasAll> aliasAllList = view.getAliasAllList()
        if (aliasAllList) {
            List<ViewEntityMember> members = view.getMemberEntities()
            aliasAllList.each { aliasAllElmt ->
                String currentPrefix = "${prefix ?: ''}${aliasAllElmt.getPrefix().getValue() ?: ''}" as String
                String entityName = getEntityNameFromAlias(aliasAllElmt, members)
                if (entityName) {
                    List<String> currentExcludedFields = getListOfExcludedFieldNames(aliasAllElmt)
                    if (currentExcludedFields) currentExcludedFields.addAll(excludedFields)
                    Entity currentEntity = structureService.getEntity(entityName)
                    if (currentEntity) {
                        generateLookupsWithEntity(currentEntity, currentPrefix, currentExcludedFields, result)
                    } else {
                        ViewEntity currentView = structureService.getViewEntity(entityName)
                        generateLookupsWithView(currentView, currentPrefix, structureService, currentExcludedFields, result, index + 1)
                    }
                }
            }
        }
        generateLookupElementsFromName(aliases, result)
    }

    private static String getEntityNameFromAlias(AliasAll aliasAllElmt, List<ViewEntityMember> members) {
        String alias = aliasAllElmt.getEntityAlias()
        return members.find { it.getEntityAlias().getValue() == alias }?.getEntityName()
    }

    private static List<String> getListOfExcludedFieldNames(AliasAll aliasAllElmt) {
        return aliasAllElmt.getAliasAllExcludes().collect { it.getField().getValue() }
    }

    private static void generateLookupsWithEntity(Entity entity, result) {
        generateLookupsWithEntity(entity, null, [], result)
    }

    private static void generateLookupsWithEntity(Entity entity, String prefix, List<String> excludedFields, result) {
        List<EntityField> fields = entity.getFields().findAll { entityField ->
            !excludedFields.contains(entityField.getName().getValue())
        }
        generateLookupElementsFromName(fields, prefix, result)
    }

    private static generateLookupElementsFromName(aliases, result) {
        generateLookupElementsFromName(aliases, null, result)
    }

    private static generateLookupElementsFromName(List<DomElement> fields, String prefix, CompletionResultSet result) {
        fields.forEach {
            String fieldName = "${prefix ?: ''}${it.getName()}"
            if (fieldName) createFieldLookupElement(fieldName, result)
        }
    }

    private static String retrieveEntityOrViewName(String declarationExpr) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declarationExpr)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

    private static void createFieldLookupElement(String fieldName, CompletionResultSet result) {
        LookupElement el = LookupElementBuilder.create(fieldName)
        result.addElement(PrioritizedLookupElement.withPriority(el, 1000))
    }
}
