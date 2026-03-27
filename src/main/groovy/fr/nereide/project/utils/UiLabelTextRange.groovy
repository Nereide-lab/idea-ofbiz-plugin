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
package fr.nereide.project.utils

import static fr.nereide.project.pattern.OfbizPluginConstants.CLOSING_BRACKET
import static fr.nereide.project.pattern.OfbizPluginConstants.DOT_MAP_PROP_ACCESSOR
import static fr.nereide.project.pattern.OfbizPluginConstants.DYNAMIC_STRING_DOLLAR

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Small utility class for readability
 */
class UiLabelTextRange extends TextRange {

    UiLabelTextRange(PsiElement element) {
        super(element.text.indexOf(DOT_MAP_PROP_ACCESSOR) + 1, element.text.indexOf(CLOSING_BRACKET))
    }

    UiLabelTextRange(PsiElement element, int index) {
        super(getIndexOf(element, index, DOT_MAP_PROP_ACCESSOR) + 1, getIndexOf(element, index, CLOSING_BRACKET))
    }

    static int getIndexOf(PsiElement element, int index, String str) {
        String fullText = element.text
        String[] parts = fullText.split(DYNAMIC_STRING_DOLLAR).collect(part -> part.trim() ?: null)
        String relevantPart = parts[index]
        return fullText.indexOf(
                str,
                fullText.indexOf(relevantPart),
                fullText.split(DYNAMIC_STRING_DOLLAR).take(index + 1).join('').size() + index
        )
    }

}
