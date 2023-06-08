/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.project.worker

import com.intellij.util.xml.DomElement
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface

import static org.apache.ofbiz.idea.plugin.dom.EntityModelFile.*

class EntityWorker {

    /**
     * Returns a String list of all entity fields
     * @param entity
     * @return
     */
    static List<String> getEntityFields(Entity entity) {
        return getEntityFields(entity, null, [])
    }

    static List<String> getEntityFields(Entity entity, String prefix, List<String> excludedFields) {
        List<EntityField> fields = entity.getFields().findAll { entityField ->
            !excludedFields.contains(entityField.getName().getValue())
        }
        return getFormatedFieldsName(fields, prefix)
    }

    /**
     * Returns a String list of all entity fields with prefixes, and nested views included in the search
     * @param view
     * @param structureService
     * @param index
     * @return
     */
    static List<String> getViewFields(ViewEntity view, ProjectServiceInterface structureService, int index) {
        return getViewFields(view, null, structureService, [], index)
    }

    static List<String> getViewFields(ViewEntity view, String prefix, ProjectServiceInterface structureService,
                                      List<String> excludedFields, int index) {
        List<String> fieldsList = []
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
                        fieldsList.addAll(getEntityFields(currentEntity, currentPrefix, currentExcludedFields))
                    } else {
                        ViewEntity currentView = structureService.getViewEntity(entityName)
                        List<String> viewFields = getViewFields(currentView, currentPrefix, structureService, currentExcludedFields, index + 1)
                        if (viewFields) fieldsList.addAll(viewFields)
                    }
                }
            }
        }
        fieldsList.addAll(getFormatedFieldsName(aliases))
        return fieldsList.unique()
    }

    static String getEntityNameFromAlias(AliasAll aliasAllElmt, List<ViewEntityMember> members) {
        String alias = aliasAllElmt.getEntityAlias()
        return members.find { it.getEntityAlias().getValue() == alias }?.getEntityName()
    }

    static List<String> getListOfExcludedFieldNames(AliasAll aliasAllElmt) {
        return aliasAllElmt.getAliasAllExcludes().collect { it.getField().getValue() }
    }


    static List<String> getFormatedFieldsName(aliases) {
        return getFormatedFieldsName(aliases, null)
    }

    static List<String> getFormatedFieldsName(List<DomElement> fields, String prefix) {
        return fields.stream().map { DomElement field ->
            "${prefix ?: ''}${field.getName()}"
        }.toList() as List<String>
    }
}
