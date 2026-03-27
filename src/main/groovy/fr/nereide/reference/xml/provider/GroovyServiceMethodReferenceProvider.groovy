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
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.reference.xml.GroovyServiceDefReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class GroovyServiceMethodReferenceProvider extends JavaMethodReferenceProvider {

    @Override
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (PluginActivator.getInstance(element.project).inactive) return []
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
        String locationAttr = getClassLocation(element)
        if (!locationAttr) return PsiReference.EMPTY_ARRAY
        PsiFile targetedFile = ph.getPsiFileAtLocation(locationAttr)
        if (!targetedFile || !(targetedFile instanceof GroovyFile)) return PsiReference.EMPTY_ARRAY
        return new GroovyServiceDefReference(element as XmlAttributeValue,
                (targetedFile as GroovyFile).scriptClass,
                true)
    }

}
