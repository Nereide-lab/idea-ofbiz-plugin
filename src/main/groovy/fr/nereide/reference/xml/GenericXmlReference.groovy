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

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.DomManager
import fr.nereide.project.ProjectServiceInterface

abstract class GenericXmlReference extends PsiReferenceBase<XmlAttributeValue> {

    DomManager dm
    ProjectServiceInterface ps

    GenericXmlReference(XmlAttributeValue element, TextRange textRange, boolean soft) {
        super(element, textRange, soft)
        dm = DomManager.getDomManager(element.getProject())
        ps = element.getProject().getService(ProjectServiceInterface.class)
    }

    GenericXmlReference(XmlAttributeValue element, boolean soft) {
        super(element, soft)
        dm = DomManager.getDomManager(element.getProject())
        ps = element.getProject().getService(ProjectServiceInterface.class)
    }

}
