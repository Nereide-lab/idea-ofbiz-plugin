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
package fr.nereide.completion.provider.xml

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

/**
 * Entity fields completion provider for Java
 */
class XmlEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    public static final String E_ALIAS = 'entity-alias'
    final Class assigmentClass = null
    final Class referenceExpressionClass = null
    final Class methodExprClass = null

    // codenarc-disable UnusedMethodParameter
    String getAssigmentString(PsiElement assign) { return '' }

    PsiElement[] getMethodArgs(PsiElement method) { return PsiElement.EMPTY_ARRAY }
    // codenarc-enable UnusedMethodParameter

    @Override
    String getEntityNameFromPsiElement(PsiElement psiElement) {
        String entityAlias = null
        XmlTag initialContainingTag = getParentOfType(psiElement, XmlTag)
        XmlTag fullView = null

        switch (initialContainingTag.name) {
            case 'alias':
                entityAlias = initialContainingTag.getAttribute(E_ALIAS)?.value
                if (!entityAlias) return ''
                fullView = getParentOfType(initialContainingTag, XmlTag)
                break
            case 'key-map':
                XmlTag viewLinkTag = getParentOfType(initialContainingTag, XmlTag)
                entityAlias = getParentOfType(psiElement, XmlAttribute).name == 'field-name' ?
                        viewLinkTag.getAttributeValue(E_ALIAS) :
                        viewLinkTag.getAttributeValue('rel-entity-alias')
                fullView = getParentOfType(viewLinkTag, XmlTag)
                break
            default: // cas du fichier de chargement de données
                return initialContainingTag.name
        }

        if (!entityAlias || !fullView) return ''

        XmlTag relevantMember = List.of(fullView.subTags).find { tag ->
            tag.name == 'member-entity' && tag.getAttribute(E_ALIAS)?.value == entityAlias
        }
        return relevantMember.getAttribute('entity-name')?.value
    }

}
