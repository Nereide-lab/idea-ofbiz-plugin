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


import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.nereide.project.OfbizClassUtil
import fr.nereide.project.PluginActivator
import fr.nereide.reference.xml.JavaMethodReference
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class JavaMethodReferenceProvider extends PsiReferenceProvider {

    static String getClassLocation(PsiElement element) {
        XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag)
        XmlAttribute locationAttr = tag.getAttribute('location') ?: tag.getAttribute('path')
        return locationAttr ? locationAttr.value : null
    }
    /* codenarc-disable UnusedMethodParameter */

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        /* codenarc-enable UnusedMethodParameter */
        if (PluginActivator.getInstance(element.project).inactive) return PsiReference.EMPTY_ARRAY
        if (!FileReferenceProvider.isJavaService(element)) {
            return PsiReference.EMPTY_ARRAY
        }
        String classLocation = getClassLocation(element)
        if (!classLocation) return PsiReference.EMPTY_ARRAY
        PsiClass aClass = OfbizClassUtil.findClass(element.project, classLocation)
        return (PsiReference) new JavaMethodReference((XmlAttributeValue) element, aClass, true)
    }

}
