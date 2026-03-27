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
package fr.nereide.documentation.format

import static com.intellij.lang.documentation.DocumentationMarkup.CONTENT_ELEMENT
import static com.intellij.lang.documentation.DocumentationMarkup.GRAYED_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.Element
import static com.intellij.openapi.util.text.HtmlChunk.text

import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.EntityField
import fr.nereide.dom.element.entitymodel.EntityPrimKey
import fr.nereide.dom.element.entitymodel.EntityRelation
import fr.nereide.dom.element.entitymodel.ExtendEntity
import com.intellij.openapi.util.text.HtmlBuilder
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.MiscUtils

/**
 * Formatter for documentation
 */
class OfbizEntityDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatExtendEntityListForEntity(String entityName, OfbizProjectHelper ph) {
        HtmlBuilder builder = new HtmlBuilder()
        List<ExtendEntity> extendList = ph.getExtendEntityListForEntity(entityName)
        extendList.forEach { extendEntity ->
            List<EntityField> extendedFields = extendEntity.fields
            String fileName = extendEntity.xmlElement.containingFile.name
            HtmlBuilder extendBuilder = new HtmlBuilder()
                    .append(text('Extended ').bold())
                    .nbsp()
                    .append(text("in file ${fileName} [component ${MiscUtils.getComponentName(extendEntity)}]"))
                    .append(text(', with fields: '))
                    .append(formatExtendFields(extendedFields))
            builder.append(extendBuilder)
        }
        return builder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatEntityFieldList(String entityName, OfbizProjectHelper ph) {
        HtmlBuilder builder = new HtmlBuilder()
        Entity entity = ph.getEntity(entityName)
        List<EntityField> fields = entity.fields
        List<EntityPrimKey> pks = entity.primKeys

        HtmlBuilder pkListBuilder = new HtmlBuilder()
        builder.append(text('Primary keys:').bold()).nbsp()
        fields.findAll { field -> isInPkList(pks, field) }
                .forEach { field ->
                    pkListBuilder.append(new HtmlBuilder()
                            .append(text("${field.name.value}"))
                            .nbsp()
                            .append(text("${field.type.value}").wrapWith(GRAYED_ELEMENT))
                            .wrapWith(LIST_TAG)
                    )
                }
        return builder.append(pkListBuilder.wrapWith(U_LIST_TAG)).wrapWith(CONTENT_ELEMENT)
    }

    static Element formatEntityRelations(String entityName, OfbizProjectHelper ph) {
        HtmlBuilder builder = new HtmlBuilder()
        Entity entity = ph.getEntity(entityName)
        List<EntityRelation> relations = entity.relations

        builder.append(text('Related to:').bold()).nbsp()
        HtmlBuilder relListBuilder = new HtmlBuilder()
        relations.forEach { entityRelation ->
            HtmlBuilder relBuilder = new HtmlBuilder()
                    .append(text("${entityRelation.relEntityName.value}"))
                    .append(text("[${entityRelation.type.value}]").italic().wrapWith(GRAYED_ELEMENT))
            relListBuilder.append(relBuilder.wrapWith(LIST_TAG))
        }
        return builder.append(relListBuilder).wrapWith(CONTENT_ELEMENT)
    }

    private static boolean isInPkList(List<EntityPrimKey> pks, EntityField it) {
        return pks.any { pk -> pk.field.value == it.name.value }
    }

    private static Element formatExtendFields(List<EntityField> extendedFields) {
        HtmlBuilder fieldListBuilder = new HtmlBuilder()
        extendedFields.forEach { field ->
            HtmlBuilder fieldBuilder = new HtmlBuilder()
            fieldBuilder.append(text("${field.name}"))
            fieldBuilder.nbsp()
            if (field.type.value != '') {
                fieldBuilder.append(text("${field.type.value}").wrapWith(GRAYED_ELEMENT))
            }
            fieldListBuilder.append(fieldBuilder.wrapWith(LIST_TAG))
        }
        return fieldListBuilder.wrapWith(U_LIST_TAG)
    }

}
