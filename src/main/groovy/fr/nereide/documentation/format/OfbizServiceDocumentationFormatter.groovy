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
import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_ELEMENT
import static com.intellij.lang.documentation.DocumentationMarkup.GRAYED_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.Element
import static com.intellij.openapi.util.text.HtmlChunk.nbsp
import static com.intellij.openapi.util.text.HtmlChunk.text
import static fr.nereide.project.worker.ServiceWorker.getOptionalInParameters
import static fr.nereide.project.worker.ServiceWorker.getRequiredInParameters
import static fr.nereide.project.worker.ServiceWorker.SERVICE_ATTR_NAME
import static fr.nereide.project.worker.ServiceWorker.SERVICE_ATTR_TYPE

import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.StringUtil
import fr.nereide.dom.element.service.Service
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.MiscUtils

/**
 * Part of the Documentation provider system.
 */
class OfbizServiceDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatServiceDefinition(Service service) {
        HtmlBuilder definitionBuilder = new HtmlBuilder()
        // Ligne 1
        definitionBuilder.append(text("<service name=\"${service.name}\"").bold())
                .append(nbsp())
                .append(text("engine=\"${service.engine}\""))
                .append(nbsp())
                .append(text("auth=\"${service.auth.value != null ?: 'Not defined'}\""))
                .append(nbsp())
                .br()

        // Ligne 2
        if (service.defaultEntityName.value) {
            definitionBuilder.append(text("default-entity-name=\"${service.defaultEntityName}\""))
                    .append(nbsp())
        }
        if (service.location.value) {
            definitionBuilder.append(text("location=\"${service.location}\""))
                    .append(nbsp())
        }
        if (service.invoke.value) {
            definitionBuilder.append(text("invoke=\"${service.invoke}\""))
        }
        definitionBuilder.append('/>')
                .br()

        // ligne 3
        definitionBuilder.append(text("Defined in component ${MiscUtils.getComponentName(service)}"))
        return definitionBuilder.wrapWith('pre').wrapWith(DEFINITION_ELEMENT)
    }

    static Element formatServiceDescription(Service service) {
        HtmlBuilder descriptionContentBuilder = new HtmlBuilder()
        descriptionContentBuilder.append(text('Description: ').bold())
                .append(text(StringUtil.notNullize(service.description.value, 'No service description found')))
        return descriptionContentBuilder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatServiceAttributes(Service service, OfbizProjectHelper ph) {
        HtmlBuilder attributesBuilder = new HtmlBuilder()
        attributesBuilder.append(text('Service parameters'))
                .br()
                .append(text('Required:'))

        HtmlBuilder requiredAttributesBuilder = new HtmlBuilder()
        formatAttributeListForDisplay(true, service, ph, requiredAttributesBuilder)
        attributesBuilder.append(requiredAttributesBuilder.wrapWith(U_LIST_TAG))
                .append(text('Optional:'))

        HtmlBuilder optionalAttributesBuilder = new HtmlBuilder()
        formatAttributeListForDisplay(false, service, ph, optionalAttributesBuilder)
        return attributesBuilder.append(optionalAttributesBuilder.wrapWith(U_LIST_TAG))
                .wrapWith(CONTENT_ELEMENT)
    }

    static void formatAttributeListForDisplay(boolean required, Service service, OfbizProjectHelper ph,
                                              HtmlBuilder attributesBuilder) {
        List<Map> optionalAttributes = required ? getRequiredInParameters(service, ph) :
                getOptionalInParameters(service, ph)
        optionalAttributes.forEach { optAttr ->
            HtmlBuilder attrBuilder = new HtmlBuilder()
                    .append(text("${optAttr.get(SERVICE_ATTR_NAME)}"))
            if (optAttr.get(SERVICE_ATTR_TYPE)) {
                attrBuilder.nbsp()
                attrBuilder.append(text("[${optAttr.get(SERVICE_ATTR_TYPE)}]").wrapWith(GRAYED_ELEMENT))
            }
            attributesBuilder.append(attrBuilder.wrapWith('li'))
        }
    }

    static Element formatServiceImplements(Service service) {
        List implementsList = service.implements
        HtmlBuilder implementsBuilder = new HtmlBuilder()
        implementsBuilder.append(text('Implements:').bold()).append(nbsp())
        for (int i = 0; i < implementsList.size() - 1; i++) {
            implementsBuilder.append(text(implementsList[i].service.value))
                    .append(text(COMMA)).append(nbsp())
        }
        implementsBuilder.append(implementsList.last().service.value)
        return implementsBuilder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatServiceGroupInvoke(Service service) {
        List invokedServices = service.group.invokes
        HtmlBuilder groupBuilder = new HtmlBuilder()
        groupBuilder.append(text('Service group invokes:').bold()).append(nbsp())
        for (int i = 0; i < invokedServices.size() - 1; i++) {
            groupBuilder.append(text(invokedServices[i].name.value))
                    .append(text(COMMA)).append(nbsp())
        }
        groupBuilder.append(invokedServices.last().name.value)
        return groupBuilder.wrapWith(CONTENT_ELEMENT)
    }

}
