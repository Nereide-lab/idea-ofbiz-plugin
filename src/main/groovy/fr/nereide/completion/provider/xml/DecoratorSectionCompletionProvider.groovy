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
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.nereide.dom.element.screen.IncludeScreen
import fr.nereide.dom.element.screen.Screen
import fr.nereide.dom.element.screen.ScreenSection
import fr.nereide.dom.element.screen.ScreenWidget
import fr.nereide.dom.file.ScreenFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin completion system
 */
class DecoratorSectionCompletionProvider extends CompletionProvider<CompletionParameters> {

    public static final String DECORATOR_SCREEN = 'decorator-screen'
    public static final String DECORATOR_SECTION = 'decorator-section'
    public static final String NAME_ATTR = 'name'
    public static final int ARCHI_THRESHOLD = 4

    static Screen getDecoratorScreenFromContext(OfbizProjectHelper ph, XmlElement myAttrValue) {
        XmlTag decoratorSectionTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag, false)
        int i = 0
        while (decoratorSectionTag.localName != DECORATOR_SECTION && i < ARCHI_THRESHOLD) {
            decoratorSectionTag = PsiTreeUtil.getParentOfType(decoratorSectionTag, XmlTag, false)
            i++
        }
        if (decoratorSectionTag.localName != DECORATOR_SECTION) return
        XmlTag decoratorScreenTag = decoratorSectionTag.parentTag
        i = 0
        while (decoratorSectionTag.parentTag &&
                decoratorScreenTag.localName != DECORATOR_SCREEN && i < ARCHI_THRESHOLD) {
            decoratorScreenTag = decoratorSectionTag.parentTag
            i++
        }
        if (decoratorScreenTag.localName != DECORATOR_SCREEN) return
        String screenLocation = decoratorScreenTag.getAttributeValue('location')
        String decoratorName = decoratorScreenTag.getAttributeValue(NAME_ATTR)
        return ph.getScreenFromFileAtLocation(screenLocation, decoratorName)
    }

    // codenarc-disable UnusedMethodParameter
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.position
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        Screen decoratorScreen = getDecoratorScreenFromContext(ph, myAttrValue)
        if (!decoratorScreen) return
        List<IncludeScreen> includes = getIncludesInScreenDom(decoratorScreen)

        List<XmlAttribute> decoratorSections = includes.collect { IncludeScreen incScrTag ->
            getDecoratorSectionsFromInclude(incScrTag, ph, decoratorScreen.name.value)
        }?.flatten()?.findAll { include -> include != null }

        decoratorSections.forEach { XmlAttribute attr ->
            String componentName = MiscUtils.getComponentName(attr)
            String fileName = attr.containingFile.name
            LookupElement lookupElement = LookupElementBuilder.create(attr.value)
                    .withTailText("  $fileName [$componentName]" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement,
                    OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY))
        }
    }

    private static List<XmlAttribute> getDecoratorSectionsFromInclude(IncludeScreen incScreenDom, OfbizProjectHelper ph,
                                                                      String decoratorName) {
        List result = []
        XmlAttribute screenNameXmlValue = incScreenDom.name.xmlElement as XmlAttribute
        XmlTag resolvedScreen = new ScreenReference(screenNameXmlValue.valueElement).resolve() as XmlTag
        if (!resolvedScreen) {
            PsiDirectory commonThemeDir = ph.getComponentDir('common-theme')
            if (!commonThemeDir) return result
            XmlFile commonScreenFile = commonThemeDir.findSubdirectory('widget')
                    .findFile('CommonScreens.xml') as XmlFile
            ScreenFile screenFile = ph.getDomManager() // codenarc-disable UnnecessaryGetter
                    .getFileElement(commonScreenFile, ScreenFile)
                    ?.rootElement
            resolvedScreen = screenFile.screens.find { screen ->
                screen.name.value == decoratorName
            }?.xmlElement as XmlTag
        }
        if (!resolvedScreen) return result

        List<IncludeScreen> includes = getIncludesInScreenDom(
                ph.getDomManager().getDomElement(resolvedScreen) as Screen) // codenarc-disable UnnecessaryGetter
        if (includes) {
            includes.forEach { include ->
                result << getDecoratorSectionsFromInclude(include, ph, include.name.value)
            }
        }

        result << PsiTreeUtil.collectElements(resolvedScreen, sectionTagFilter)*.getAttribute(NAME_ATTR)
        return result
    }

    private static PsiElementFilter getSectionTagFilter() {
        return new PsiElementFilter() {

            @Override
            boolean isAccepted(@NotNull PsiElement psiElement) {
                boolean isTag = XmlTag.isAssignableFrom(psiElement.class)
                if (!isTag) return false
                return isTag && ((psiElement as XmlTag).localName == 'decorator-section-include')
            }

        }
    }

    private static List<IncludeScreen> getIncludesInScreenDom(Screen decoratorScreen) {
        return decoratorScreen.sections?.flatten()
                ?.collectMany { ScreenSection scrSec -> scrSec.widgets }
                ?.collectMany { ScreenWidget scrWid -> scrWid.includeScreens }
    }

}
