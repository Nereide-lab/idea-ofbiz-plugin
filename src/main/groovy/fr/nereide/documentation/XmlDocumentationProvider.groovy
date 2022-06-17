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
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class XmlDocumentationProvider extends AbstractDocumentationProvider {
    private static final Logger LOG = Logger.getInstance(XmlDocumentationProvider.class)

    @Override
    String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (!element instanceof XmlTag) return null
        ProjectServiceInterface structureService = element.getProject().getService(ProjectServiceInterface.class)
        XmlTag tag = element as XmlTag
        String elementName = (originalElement as XmlAttributeValue).getValue()

        switch (tag.getLocalName()) {
            case 'entity':
                return structureService.getEntity(elementName).getDescription().getValue()
            case 'view-entity':
                return structureService.getViewEntity(elementName).getDescription().getValue()
            case 'service':
                return structureService.getService(elementName).getDescription().getValue()
            default : return null
        }
    }

    @Override
    String generateHoverDoc(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        return null
    }
}