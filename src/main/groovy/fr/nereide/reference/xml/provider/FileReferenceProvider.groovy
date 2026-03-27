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
package fr.nereide.reference.xml.provider

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.converters.ClassValueConverterImpl
import fr.nereide.project.PluginActivator
import fr.nereide.reference.common.ComponentAwareFileReferenceSet
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class FileReferenceProvider extends PsiReferenceProvider {

    public static final String TYPE = 'type'
    public static final String ENGINE = 'engine'
    public static final String JAVA = 'java'

    static boolean isJavaService(PsiElement element) {
        XmlAttribute parent
        try {
            parent = element.parent as XmlAttribute
        } catch (ClassCastException ignored) {
            return false
        }
        return hasJavaEngine(parent) || hasJavaType(parent)
    }

    /* codenarc-disable UnusedMethodParameter */

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        /* codenarc-enable UnusedMethodParameter */
        if (PluginActivator.getInstance(element.project).inactive) return []
        if (isJavaService(element)) {
            return ClassValueConverterImpl
                    .classValueConverter
                    .createReferences(null, element, null) ?: PsiReference.EMPTY_ARRAY
        }
        return ComponentAwareFileReferenceSet
                .make(element, true)
                .allReferences ?: PsiReference.EMPTY_ARRAY
    }

    private static boolean hasJavaType(XmlAttribute parent) {
        return parent.parent.getAttributeValue(TYPE) != null &&
                parent.parent.getAttributeValue(TYPE).equalsIgnoreCase(JAVA)
    }

    private static boolean hasJavaEngine(XmlAttribute parent) {
        return parent.parent.getAttributeValue(ENGINE) != null &&
                parent.parent.getAttributeValue(ENGINE).equalsIgnoreCase(JAVA)
    }

}
