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

import fr.nereide.dom.EntityModelFile

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
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
import fr.nereide.dom.EntityModelFile.AliasAllExclude
import fr.nereide.dom.EntityModelFile.AliasAll
import fr.nereide.dom.EntityModelFile.Alias
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.EntityField
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable

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
            ViewEntity view = structureService.getViewEntity(entityName)

            if (entity) {
                generateLookupWithEntity(entity, result)
            } else if (view) {
                // TODO : gérer le cas de vue d'entité de vue d'entité
                String viewName = view.getEntityName()
                List<EntityAliasObject> memberEntities = getMemberEntities(view, structureService)
                List<AliasAll> aliasAlls = view.getAliasAllList()
                List<Alias> aliases = view.getAliases()
                if (aliasAlls.size() > 0) {
                    aliasAlls.forEach { aliasAllEl ->
                        String prefix = aliasAllEl.getPrefix()
                        String curentALias = aliasAllEl.getEntityAlias()
                        if (!hasExcludes(aliasAllEl)) {
                            Entity currentEntity = memberEntities.find { it.entityAlias == curentALias }.getEntity()
                            if (prefix) generateLookupFromViewWithEntityAndPrefix(currentEntity, viewName, prefix, result)
                            else generateLookupFromViewWithEntity(currentEntity, viewName, result)
                        } else {
                            List<AliasAllExclude> excludedFields = aliasAllEl.getAliasAllExcludes()
                            Entity currentEntity = memberEntities.find { it.entityAlias == curentALias }.getEntity()
                            if (prefix) generateLookupFromViewWithEntityAndExcludesAndPrefix(currentEntity, viewName, prefix, excludedFields, result)
                            else generateLookupFromViewWithEntityAndExcludes(currentEntity, viewName, excludedFields, result)
                        }
                    }
                }
                if(aliases.size() > 0) {
                    aliases.forEach { alias ->
                        String presentedName = alias.getName()
                        String originEntityName = memberEntities.find { it.entityAlias == alias.getEntityAlias() as String }
                                .getEntity()
                                .getEntityName()
                        createFieldLookupElement("${viewName}.${originEntityName}", presentedName, result)
                    }
                }
            }
        } catch (ProcessCanceledException e) {
            LOG.info(e)
        } catch (Exception e) {
            LOG.error(e)
        }

        // TODO : penser à virer cet ajout
        LookupElement el = LookupElementBuilder.create('GENERIC_VALUE_ATTRIBUTE_TROLO')
        result.addElement(PrioritizedLookupElement.withPriority(el, 3000))
    }

    private List<EntityAliasObject> getMemberEntities(ViewEntity view, ProjectServiceInterface structureService) {
        List<EntityAliasObject> memberEntities = []
        view.getMemberEntities().forEach {
            String currentEntityName = it.getEntityName().getRawText()
            String currentEntityAlias = it.getEntityAlias().getRawText()
            if (currentEntityName) {
                memberEntities << (new EntityAliasObject(currentEntityName,
                        currentEntityAlias, structureService.getEntity(currentEntityName))
                )
            }
        }
        return memberEntities
    }

    private static void generateLookupWithEntity(Entity entity, result) {
        List<EntityField> fields = entity.getFields()
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName) createFieldLookupElement(entity.getEntityName().getRawText(), fieldName, result)
        }
    }

    private static void generateLookupFromViewWithEntity(Entity entity, String viewName, result) {
        List<EntityField> fields = entity.getFields()
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName) createFieldLookupElement("${viewName}.${entity.getEntityName()}", fieldName, result)
        }
    }

    static void generateLookupFromViewWithEntityAndExcludesAndPrefix(Entity entity, String viewName, String prefix,
                                                                     List<AliasAllExclude> aliasAllExcludes, CompletionResultSet result) {
        List<String> excludedFields = aliasAllExcludes.stream().map {
            it.getExcludedField()
        }.collect(Collectors.toList())

        List<EntityField> fields = entity.getFields()
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName && !isExcluded(excludedFields, fieldName)) {
                createFieldLookupElement("${viewName}.${entity.getEntityName()}", prefix + fieldName, result)
            }
        }
    }

    static void generateLookupFromViewWithEntityAndExcludes(Entity entity, String viewName,
                                                            List<AliasAllExclude> aliasAllExcludes, CompletionResultSet resultSet) {
        List<String> excludedFields = aliasAllExcludes.stream().map { it.getExcludedField() }
                .collect(Collectors.toList())

        List<EntityField> fields = entity.getFields()
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName && !isExcluded(excludedFields, fieldName)) {
                createFieldLookupElement("${viewName}.${entity.getEntityName()}", fieldName, resultSet)
            }
        }
    }

    static void generateLookupFromViewWithEntityAndPrefix(Entity entity, String viewName, String prefix, CompletionResultSet result) {
        List<EntityField> fields = entity.getFields()
        fields.forEach {
            String fieldName = it.getName()
            if (fieldName) createFieldLookupElement("${viewName}.${entity.getEntityName()}", prefix + fieldName, result)
        }
    }

    private static boolean isExcluded(List<String> excludedFields, String fieldName) {
        excludedFields.contains(fieldName)
    }

    private static boolean hasExcludes(AliasAll alias) {
        alias.getAliasAllExcludes()
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


    /**
     * Small utility object
     */
    class EntityAliasObject {
        private final String entityName
        private final String entityAlias
        private final Entity entityElement

        EntityAliasObject(String entityName, String entityAlias, Entity entityElement) {
            this.entityName = entityName
            this.entityAlias = entityAlias
            this.entityElement = entityElement
        }

        String getName() { return entityName }

        String getAlias() { return entityAlias }

        Entity getEntity() { return entityElement }
    }
}
