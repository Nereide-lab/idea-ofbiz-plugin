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

import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.text
import static com.intellij.openapi.util.text.HtmlChunk.nbsp
import static com.intellij.openapi.util.text.HtmlChunk.Element

import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.util.xml.DomElement
import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.ViewEntity
import fr.nereide.project.utils.MiscUtils

/**
 * Documentation formater for hover doc
 */
class OfbizCommonDocumentationFormatter {

    protected static final String LIST_TAG = 'li'
    protected static final String U_LIST_TAG = 'ul'
    public static final String COMMA = ','

    static String formatNavigateDocWithDomElement(DomElement element, String elementType, String elementName) {
        HtmlBuilder docBuilder = new HtmlBuilder()
        String containingFile = element.xmlElement.containingFile.name
        docBuilder.append(text("Ofbiz ${elementType}: ${elementName}"))
                .br()
                .append(text("Defined in ${containingFile} [component: ${MiscUtils.getComponentName(element)}]"))
        return docBuilder.toString()
    }

    static Element formatEntityOrViewDefinition(DomElement element) {
        boolean isView = element instanceof ViewEntity
        boolean isEntity = element instanceof Entity
        if (!isView && !isEntity) return null
        HtmlBuilder builder = new HtmlBuilder()

        String fileName = element.xmlElement.containingFile.name
        builder.append(text("<${isView ? 'view' : 'entity'} entity-name=\"${element.entityName}\"").bold())
                .append(nbsp())
                .append(text("package-name=\"${element.packageName}\""))
                .append(nbsp())
                .append(text("title=\"${element.title}\" />"))
                .br()
                .append(text("Description: ${element.description}"))
                .br()
                .append(text("Defined in ${fileName}, [Component: ${MiscUtils.getComponentName(element)}]"))
        return builder.wrapWith('pre').wrapWith(DEFINITION_ELEMENT)
    }

}
