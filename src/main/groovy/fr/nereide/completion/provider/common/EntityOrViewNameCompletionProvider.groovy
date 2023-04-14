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

package fr.nereide.completion.provider.common

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

class EntityOrViewNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {

        ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        addEntitiesLookup(structureService, result)
        addViewEntitiesLookups(structureService, result)
    }

    static void addEntitiesLookup(ProjectServiceInterface structureService, CompletionResultSet result) {
        List entities = structureService.getAllEntities()
        for (Entity entity : entities) {
            LookupElement lookupElement = LookupElementBuilder.create(entity.getEntityName())
                    .withIcon(PluginIcons.ENTITY_ICON)
                    .withTailText(" Component:${MiscUtils.getComponentName(entity)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }

    static void addViewEntitiesLookups(ProjectServiceInterface structureService, CompletionResultSet result) {
        List viewEntities = structureService.getAllViewEntities()
        for (ViewEntity view : viewEntities) {
            LookupElement lookupElement = LookupElementBuilder.create(view.getEntityName())
                    .withIcon(PluginIcons.VIEW_ENTITY_ICON)
                    .withTailText(" Component:${MiscUtils.getComponentName(view)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}
