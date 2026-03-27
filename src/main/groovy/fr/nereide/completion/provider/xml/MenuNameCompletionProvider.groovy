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
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.nereide.dom.element.menu.Menu
import fr.nereide.dom.file.MenuFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin completion system
 */
class MenuNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.position
        List<Menu> menus
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag parentTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag)
        XmlAttribute locationAttr = parentTag.getAttribute('location')
        if (locationAttr) {
            XmlAttributeValue menuLocationAttr = locationAttr.valueElement
            menus = ph.getDomElementListFromFileAtLocation(menuLocationAttr.value, MenuFile)
        } else {
            menus = []
        }

        menus.each { Menu menu ->
            LookupElement lookupElement = LookupElementBuilder.create(menu.name.value as String)
                    .withTailText(" Component:${MiscUtils.getComponentName(menu as Menu)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement,
                    OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY))
        }
    }

}
