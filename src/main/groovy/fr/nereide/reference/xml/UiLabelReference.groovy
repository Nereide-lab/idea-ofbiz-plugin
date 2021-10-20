/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.DomElement
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class UiLabelReference extends PsiReferenceBase<XmlAttributeValue> {
    UiLabelReference(XmlAttributeValue element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getProperty(this.getUiLabelAwareValue())
        return definition != null ? definition.getXmlElement() : null
    }

    String getUiLabelAwareValue() {
        String text = this.getValue()
        if (text.startsWith('${')) {
            return text.substring(13, text.length() - 1)
        }
        return text
    }
}
