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

package fr.nereide.reference.xml.provider

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

import static fr.nereide.project.utils.XmlUtils.getParentTag
import static fr.nereide.project.utils.XmlUtils.isPageReferenceFromController


class ScreenReferenceProvider extends PsiReferenceProvider {
    ScreenReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (!(element instanceof XmlAttributeValue)) {
            return PsiReference.EMPTY_ARRAY
        }

        ScreenReference ref

        if (isPageReferenceFromController(getParentTag(element))) {
            TextRange range = getScreenTextRangeFromController(element)
            ref = new ScreenReference((XmlAttributeValue) element, range, true)
        } else {
            ref = new ScreenReference((XmlAttributeValue) element, true)
        }
        PsiReference[] reference = (PsiReference) ref
        return reference ?: PsiReference.EMPTY_ARRAY
    }

    static TextRange getScreenTextRangeFromController(PsiElement element) {
        String locationAndScreen = element.text.replaceAll('"', '')
        return new TextRange(locationAndScreen.lastIndexOf('#') + 2, locationAndScreen.length() + 1)
    }
}
