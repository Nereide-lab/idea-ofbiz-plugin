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

package fr.nereide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.ViewEntity
import fr.nereide.dom.element.service.Service
import fr.nereide.dom.element.uilabel.Property
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.MiscUtils

import static fr.nereide.documentation.format.OfbizCommonDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizServiceDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizEntityDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizViewDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizLabelDocumentationFormater.*

class OfbizCommonDocumentationProvider extends AbstractDocumentationProvider {

    static String getQuickNavigateInfo(PsiElement element, String elementName) {
        if (!element || !(element instanceof XmlTag)) return null
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
        return getTag(element) ? getQuickNavigateDocForElement(getTag(element), ph, elementName) : null
    }

    static String generateDoc(PsiElement element, String elementName) {
        if (!element || !(element instanceof XmlTag)) return null
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
        XmlTag tag = getTag(element)
        if (!elementName || !tag) return null
        switch (tag.getLocalName()) {
            case 'service':
                String serviceName = ph.getService(elementName)?.name?.value
                return serviceName ? generateServiceDoc(serviceName, ph) : 'Service not found'
            case 'entity':
                String entityName = ph.getEntity(elementName)?.entityName?.value
                return entityName ? generateEntityDoc(entityName, ph) : 'Entity not found'
            case 'view-entity':
                String viewName = ph.getViewEntity(elementName)?.entityName?.value
                return viewName ? generateViewDoc(viewName, ph) : 'View not found'
            case 'property':
                String propertyName = ph.getProperty(MiscUtils.getUiLabelSafeValue(elementName))?.key?.value
                return propertyName ? generateUiLabelDoc(propertyName, ph) : 'UiLabel not found'
            default: return null
        }
    }

    static String getQuickNavigateDocForElement(XmlTag tag, OfbizProjectHelper ph, String elementName) {
        switch (tag.getLocalName()) {
            case 'entity':
                return generateEntityQuickNavigateDoc(ph, elementName)
            case 'view-entity':
                return generateViewQuickNavigateDoc(ph, elementName)
            case 'service':
                return generateServiceQuickNavigateDoc(ph, elementName)
            case 'property':
                return generatePropertyQuickNavigateDoc(MiscUtils.getUiLabelSafeValue(elementName), ph)
            default: return null
        }
    }

    static String generateUiLabelDoc(String propertyName, OfbizProjectHelper ph) {
        Property property = ph.getProperty(propertyName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatPropertyDefinition(property))
                .append(formatPropertyText(property))
        return docBuilder.toString()
    }


    static String generateServiceDoc(String serviceName, OfbizProjectHelper ph) {
        Service service = ph.getService(serviceName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatServiceDefinition(service))
                .append(formatServiceDescription(service))
        if (service.getImplements().size() > 0) {
            docBuilder.append(formatServiceImplements(service))
                    .br()
        }
        if (service.getGroup().getInvokes().size() > 0) {
            docBuilder.append(formatServiceGroupInvoke(service))
                    .br()
        }
        docBuilder.append(formatServiceAttributes(service, ph))

        return docBuilder.toString()
    }

    static String generateEntityDoc(String entityName, OfbizProjectHelper ph) {
        Entity entity = ph.getEntity(entityName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatEntityDefinition(entity))
        if (entity.getFields().size() > 0) {
            docBuilder.append(formatEntityFieldList(entityName, ph))
        }
        if (ph.getExtendEntityListForEntity(entityName).size() > 0) {
            docBuilder.append(formatExtendEntityListForEntity(entityName, ph))
        }
        if (entity.getRelations().size() > 0) {
            docBuilder.append(formatEntityRelations(entityName, ph))
        }
        return docBuilder.toString()
    }

    static String generateViewDoc(String entityName, OfbizProjectHelper ph) {
        ViewEntity view = ph.getViewEntity(entityName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatViewDefinition(view))
        return docBuilder.toString()
    }

    static XmlTag getTag(PsiElement element) {
        try {
            return element as XmlTag
        } catch (ClassCastException ignored) {
            return null
        }
    }

    static String generateEntityQuickNavigateDoc(OfbizProjectHelper ph, String elementName) {
        Entity entity = ph.getEntity(elementName)
        return formatNavigateDocWithDomElement(entity, 'entity', elementName)
    }

    static String generateViewQuickNavigateDoc(OfbizProjectHelper ph, String elementName) {
        ViewEntity view = ph.getViewEntity(elementName)
        return formatNavigateDocWithDomElement(view, 'view-entity', elementName)
    }

    static String generateServiceQuickNavigateDoc(OfbizProjectHelper ph, String elementName) {
        Service service = ph.getService(elementName)
        return formatNavigateDocWithDomElement(service, 'service', elementName)
    }

    static String generatePropertyQuickNavigateDoc(String elementName, OfbizProjectHelper ph) {
        Property property = ph.getProperty(elementName)
        return formatNavigateDocWithDomElement(property, 'label', elementName)
    }
}
