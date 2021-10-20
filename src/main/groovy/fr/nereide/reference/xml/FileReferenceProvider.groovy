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
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.converters.ClassValueConverterImpl
import fr.nereide.reference.common.ComponentAwareFileReferenceSet
import org.jetbrains.annotations.NotNull

class FileReferenceProvider extends PsiReferenceProvider {
    FileReferenceProvider() {}

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (isJavaService(element)) {
            return ClassValueConverterImpl.getClassValueConverter().createReferences(null, element, null)
        } else {
            return ComponentAwareFileReferenceSet.createSet(element, true, true, false)
                    .getAllReferences()
        }
    }

    static boolean isJavaService(PsiElement element) {
        if (element.getParent() instanceof XmlAttribute) {
            XmlAttribute parent = (XmlAttribute) element.getParent()
            if (parent.getParent().getAttributeValue("engine") != null && parent.getParent()
                    .getAttributeValue("engine")
                    .equalsIgnoreCase("java")) {
                return true
            }
            if (parent.getParent().getAttributeValue("type") != null && parent.getParent()
                    .getAttributeValue("type")
                    .equalsIgnoreCase("java")) {
                return true
            }
        }
        return false
    }
}
