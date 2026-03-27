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

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.controller.RequestMap
import fr.nereide.dom.file.CompoundFile
import fr.nereide.dom.file.FormFile
import fr.nereide.dom.file.MenuFile
import fr.nereide.dom.file.ScreenFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.utils.XmlUtils
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin completion system
 */
class RequestUriCompletionProvider extends CompletionProvider<CompletionParameters> {

    // codenarc-disable UnusedMethodParameter
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        // codenarc-enable UnusedMethodParameter
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        DomManager dm = ph.getDomManager() // codenarc-disable UnnecessaryGetter
        PsiElement myElement = parameters.position
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        Class clazz
        XmlFile myFile = myAttrValue.containingFile as XmlFile
        if (!myFile) return
        if (dm.getFileElement(myFile, FormFile)) {
            clazz = FormFile
        } else if (dm.getFileElement(myFile, ScreenFile)) {
            clazz = ScreenFile
        } else if (dm.getFileElement(myFile, MenuFile)) {
            clazz = MenuFile
        } else if (dm.getFileElement(myFile, CompoundFile)) {
            clazz = CompoundFile
        } else {
            return
        }

        String targetType = XmlUtils.getParentTag(myAttrValue)?.getAttribute('target-type')?.value ?:
                XmlUtils.getParentTag(myAttrValue)?.getAttribute('url-mode')?.value

        if (targetType && targetType == 'inter-app') {
            Map<String, List<String>> mountPointAndRequestMaps = ph.collectAllMountPointsAndRequestMaps()
            mountPointAndRequestMaps.forEach { String mountPoint, List uris ->
                if (!uris) return
                uris.forEach { uri ->
                    String lookupValue = "$mountPoint/control/$uri"
                    LookupElement lookupElement = LookupElementBuilder.create(lookupValue)
                    result.addElement(PrioritizedLookupElement.withPriority(lookupElement,
                            OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY))
                }
            }
        } else {
            String componentName = MiscUtils.getComponentName(myAttrValue, clazz)
            Set<RequestMap> uris = ph.getComponentRequestMaps(componentName)
            uris.each { RequestMap req ->
                LookupElement lookupElement = LookupElementBuilder.create(req.uri.value)
                        .withTailText(" Component:${MiscUtils.getComponentName(req)}" as String, true)
                result.addElement(PrioritizedLookupElement.withPriority(lookupElement,
                        OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY))
            }
        }
    }

}
