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
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.dom.ServiceDefFile.Service
import fr.nereide.dom.UiLabelFile.Property
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils

import static fr.nereide.documentation.format.OfbizCommonDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizServiceDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizEntityDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizViewDocumentationFormatter.*
import static fr.nereide.documentation.format.OfbizLabelDocumentationFormater.*

class OfbizCommonDocumentationProvider extends AbstractDocumentationProvider {

    static String getQuickNavigateInfo(PsiElement element, String elementName) {
        if (!element || !(element instanceof XmlTag)) return null
        ProjectServiceInterface structureService = element.getProject().getService(ProjectServiceInterface.class)
        return getTag(element) ? getQuickNavigateDocForElement(getTag(element), structureService, elementName) : null
    }

    static String generateDoc(PsiElement element, String elementName) {
        if (!element || !(element instanceof XmlTag)) return null
        ProjectServiceInterface structureService = element.getProject().getService(ProjectServiceInterface.class)
        XmlTag tag = getTag(element)
        if (!elementName || !tag) return null
        switch (tag.getLocalName()) {
            case 'service':
                String serviceName = structureService.getService(elementName).getName().getValue()
                return serviceName ? generateServiceDoc(serviceName, structureService) : 'Service not found'
            case 'entity':
                String entityName = structureService.getEntity(elementName).getEntityName().getValue()
                return entityName ? generateEntityDoc(entityName, structureService) : 'Entity not found'
            case 'view-entity':
                String viewName = structureService.getViewEntity(elementName).getEntityName().getValue()
                return viewName ? generateViewDoc(viewName, structureService) : 'View not found'
            case 'property':
                String propertyName = structureService.getProperty(MiscUtils.getUiLabelSafeValue(elementName)).getKey().getValue()
                return propertyName ? generateUiLabelDoc(propertyName, structureService) : 'UiLabel not found'
            default: return null
        }
    }

    static String getQuickNavigateDocForElement(XmlTag tag, ProjectServiceInterface structureService, String elementName) {
        switch (tag.getLocalName()) {
            case 'entity':
                return generateEntityQuickNavigateDoc(structureService, elementName)
            case 'view-entity':
                return generateViewQuickNavigateDoc(structureService, elementName)
            case 'service':
                return generateServiceQuickNavigateDoc(structureService, elementName)
            case 'property':
                return generatePropertyQuickNavigateDoc(MiscUtils.getUiLabelSafeValue(elementName), structureService)
            default: return null
        }
    }

    static String generateUiLabelDoc(String propertyName, ProjectServiceInterface ps) {
        Property property = ps.getProperty(propertyName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatPropertyDefinition(property))
                .append(formatPropertyText(property))
        return docBuilder.toString()
    }


    static String generateServiceDoc(String serviceName, ProjectServiceInterface ps) {
        Service service = ps.getService(serviceName)
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
        docBuilder.append(formatServiceAttributes(service, ps))

        return docBuilder.toString()
    }

    static String generateEntityDoc(String entityName, ProjectServiceInterface ps) {
        Entity entity = ps.getEntity(entityName)
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(formatEntityDefinition(entity))
        if (entity.getFields().size() > 0) {
            docBuilder.append(formatEntityFieldList(entityName, ps))
        }
        if (ps.getExtendEntityListForEntity(entityName).size() > 0) {
            docBuilder.append(formatExtendEntityListForEntity(entityName, ps))
        }
        if (entity.getRelations().size() > 0) {
            docBuilder.append(formatEntityRelations(entityName, ps))
        }
        return docBuilder.toString()
    }

    static String generateViewDoc(String entityName, ProjectServiceInterface ps) {
        ViewEntity view = ps.getViewEntity(entityName)
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

    static String generateEntityQuickNavigateDoc(ProjectServiceInterface structureService, String elementName) {
        Entity entity = structureService.getEntity(elementName)
        return formatNavigateDocWithDomElement(entity, 'entity', elementName)
    }

    static String generateViewQuickNavigateDoc(ProjectServiceInterface structureService, String elementName) {
        ViewEntity view = structureService.getViewEntity(elementName)
        return formatNavigateDocWithDomElement(view, 'view-entity', elementName)
    }

    static String generateServiceQuickNavigateDoc(ProjectServiceInterface structureService, String elementName) {
        Service service = structureService.getService(elementName)
        return formatNavigateDocWithDomElement(service, 'service', elementName)
    }

    static String generatePropertyQuickNavigateDoc(String elementName, ProjectServiceInterface structureService) {
        Property property = structureService.getProperty(elementName)
        return formatNavigateDocWithDomElement(property, 'label', elementName)
    }
}
