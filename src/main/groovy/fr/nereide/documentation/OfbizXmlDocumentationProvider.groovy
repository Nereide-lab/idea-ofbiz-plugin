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

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class OfbizXmlDocumentationProvider extends OfbizCommonDocumentationProvider {
    private static final Logger LOG = Logger.getInstance(OfbizXmlDocumentationProvider.class)

    @Override
    String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return originalElement ? getQuickNavigateInfo(element, getElementName(originalElement)) : null
    }

    @Override
    String generateHoverDoc(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        return generateDoc(element, originalElement)
    }

    @Override
    String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        return originalElement ? generateDoc(element, getElementName(originalElement)) : null
    }

    private static String getElementName(PsiElement originalElement) {
        if (originalElement instanceof XmlAttributeValue) {
            return (originalElement as XmlAttributeValue).getValue()
        } else if (originalElement instanceof XmlToken && (originalElement as XmlToken).getTokenType().equals(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN)) {
            return (originalElement as XmlToken).getText()
        } else {
            try {
                return MiscUtils.getSafeTextValue(originalElement)
            } catch (Exception e) {
                LOG.warn("Could not find element name", e)
                return null
            }
        }
    }
}