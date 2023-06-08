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

package org.apache.ofbiz.idea.plugin.documentation.format

import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.StringUtil
import org.apache.ofbiz.idea.plugin.dom.ServiceDefFile
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface
import org.apache.ofbiz.idea.plugin.project.utils.MiscUtils

import static com.intellij.lang.documentation.DocumentationMarkup.*
import static com.intellij.openapi.util.text.HtmlChunk.*
import static org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker.*

class OfbizServiceDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatServiceDefinition(ServiceDefFile.Service service) {
        HtmlBuilder definitionBuilder = new HtmlBuilder()
        // Ligne 1
        definitionBuilder.append(text("<service name=\"${service.getName()}\"").bold())
                .append(nbsp())
                .append(text("engine=\"${service.getEngine()}\""))
                .append(nbsp())
                .append(text("auth=\"${service.getAuth().getValue() != null ?: "Not defined"}\""))
                .append(nbsp())
                .br()

        // Ligne 2
        if (service.getDefaultEntityName().getValue()) {
            definitionBuilder.append(text("default-entity-name=\"${service.getDefaultEntityName()}\""))
                    .append(nbsp())
        }
        if (service.getLocation().getValue()) {
            definitionBuilder.append(text("location=\"${service.getLocation()}\""))
                    .append(nbsp())
        }
        if (service.getInvoke().getValue()) {
            definitionBuilder.append(text("invoke=\"${service.getInvoke()}\""))
        }
        definitionBuilder.append("/>")
                .br()

        // ligne 3
        definitionBuilder.append(text("Defined in component ${MiscUtils.getComponentName(service)}"))
        return definitionBuilder.wrapWith("pre").wrapWith(DEFINITION_ELEMENT)
    }


    static Element formatServiceDescription(ServiceDefFile.Service service) {
        HtmlBuilder descriptionContentBuilder = new HtmlBuilder()
        descriptionContentBuilder.append(text('Description: ').bold())
                .append(text(StringUtil.notNullize(service.getDescription().getValue(), 'No service description found')))
        return descriptionContentBuilder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatServiceAttributes(ServiceDefFile.Service service, ProjectServiceInterface ps) {
        HtmlBuilder attributesBuilder = new HtmlBuilder()
        attributesBuilder.append(text("Service parameters"))
                .br()
                .append(text('Required:'))

        HtmlBuilder requiredAttributesBuilder = new HtmlBuilder()
        formatAttributeListForDisplay(true, service, ps, requiredAttributesBuilder)
        attributesBuilder.append(requiredAttributesBuilder.wrapWith('ul'))
                .append(text('Optional:'))

        HtmlBuilder optionalAttributesBuilder = new HtmlBuilder()
        formatAttributeListForDisplay(false, service, ps, optionalAttributesBuilder)
        return attributesBuilder.append(optionalAttributesBuilder.wrapWith('ul'))
                .wrapWith(CONTENT_ELEMENT)
    }

    static void formatAttributeListForDisplay(boolean required, ServiceDefFile.Service service, ProjectServiceInterface ps, attributesBuilder) {
        List<Map> optionalAttributes = required ? getRequiredInAttributes(service, ps) : getOptionalInAttributes(service, ps)
        optionalAttributes.forEach {
            HtmlBuilder attrBuilder = new HtmlBuilder()
                    .append(text("${it.get(SERVICE_ATTR_NAME)}"))
            if (it.get(SERVICE_ATTR_TYPE)) {
                attrBuilder.nbsp()
                attrBuilder.append(text("[${it.get(SERVICE_ATTR_TYPE)}]").wrapWith(GRAYED_ELEMENT))
            }
            attributesBuilder.append(attrBuilder.wrapWith('li'))
        }
    }

    static Element formatServiceImplements(ServiceDefFile.Service service) {
        List implementsList = service.getImplements()
        HtmlBuilder implementsBuilder = new HtmlBuilder()
        implementsBuilder.append(text("Implements:").bold()).append(nbsp())
        for (def i = 0; i < implementsList.size() - 1; i++) {
            implementsBuilder.append(text(implementsList[i].getService().getValue()))
                    .append(text(',')).append(nbsp())
        }
        implementsBuilder.append(implementsList.last().getService().getValue())
        return implementsBuilder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatServiceGroupInvoke(ServiceDefFile.Service service) {
        List invokedServices = service.getGroup().getInvokes()
        HtmlBuilder groupBuilder = new HtmlBuilder()
        groupBuilder.append(text("Service group invokes:").bold()).append(nbsp())
        for (def i = 0; i < invokedServices.size() - 1; i++) {
            groupBuilder.append(text(invokedServices[i].getName().getValue()))
                    .append(text(',')).append(nbsp())
        }
        groupBuilder.append(invokedServices.last().getName().getValue())
        return groupBuilder.wrapWith(CONTENT_ELEMENT)
    }
}
