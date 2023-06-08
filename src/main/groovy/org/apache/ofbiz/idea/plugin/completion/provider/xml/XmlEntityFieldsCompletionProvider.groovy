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

package org.apache.ofbiz.idea.plugin.completion.provider.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.apache.ofbiz.idea.plugin.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class XmlEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement psiElement) {
        String entityAlias = null
        XmlTag initialContainingTag = getParentOfType(psiElement, XmlTag.class)
        XmlTag fullView = null

        switch (initialContainingTag.getName()) {
            case 'alias':
                entityAlias = initialContainingTag.getAttribute('entity-alias')?.getValue()
                if (!entityAlias) return null
                fullView = getParentOfType(initialContainingTag, XmlTag.class)
                break
            case 'key-map':
                XmlTag viewLinkTag = getParentOfType(initialContainingTag, XmlTag.class)
                entityAlias = getParentOfType(psiElement, XmlAttribute.class).getName() == 'field-name' ?
                        viewLinkTag.getAttributeValue('entity-alias') :
                        viewLinkTag.getAttributeValue('rel-entity-alias')
                fullView = getParentOfType(viewLinkTag, XmlTag.class)
                break
            default: // cas du fichier de chargement de données
                return initialContainingTag.getName()

        }
        if (!entityAlias || !fullView) return null

        Optional<XmlTag> relevantMember = List.of(fullView.getSubTags()).stream()
                .filter { it.getName() == 'member-entity' }
                .filter { it.getAttribute('entity-alias')?.getValue() == entityAlias }
                .findFirst()
        return relevantMember.isEmpty() ? null :
                relevantMember.get().getAttribute('entity-name')?.getValue()
    }

    Class getAssigmentClass() {
        return null
    }

    Class getReferenceExpressionClass() {
        return null
    }

    String getAssigmentString(PsiElement assign) {
        return null
    }

    PsiElement[] getMethodArgs(PsiElement method) {
        return null
    }

    Class getMethodExprClass() {
        return null
    }
}