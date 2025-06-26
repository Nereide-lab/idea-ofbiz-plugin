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
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.screen.IncludeScreen
import fr.nereide.dom.element.screen.Screen
import fr.nereide.dom.element.screen.ScreenSection
import fr.nereide.dom.element.screen.ScreenWidget
import fr.nereide.dom.file.ScreenFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

class DecoratorSectionCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (!PluginActivator.getInstance(parameters.position.project).isActive()) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.position
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag decoratorSectionTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag.class, false)

        int i = 0
        while (decoratorSectionTag.getName() != 'decorator-section' && i < 4) {
            decoratorSectionTag = PsiTreeUtil.getParentOfType(decoratorSectionTag, XmlTag.class, false)
            i++
        }
        if (decoratorSectionTag.getName() != 'decorator-section') return

        XmlTag decoratorScreenTag = decoratorSectionTag.parentTag
        i = 0
        while (decoratorSectionTag.parentTag && decoratorScreenTag.name != 'decorator-screen' && i < 4) {
            decoratorScreenTag = decoratorSectionTag.parentTag
            i++
        }
        if (decoratorScreenTag.getName() != 'decorator-screen') return
        String screenLocation = decoratorScreenTag.getAttributeValue('location')
        String decoratorName = decoratorScreenTag.getAttributeValue('name')

        Screen decoratorScreen = ph.getScreenFromFileAtLocation(screenLocation, decoratorName)

        List includes = decoratorScreen.sections?.flatten()
                .collect { ScreenSection scrSec -> scrSec.widgets }?.flatten()
                .collect { ScreenWidget scrWid -> scrWid.includeScreens }?.flatten()

        List<String> decoratorSections = includes.collect { IncludeScreen incScrTag ->
            XmlAttribute screenNameXmlValue = incScrTag.name.xmlElement
            XmlTag resolvedScreen = new ScreenReference(screenNameXmlValue.valueElement).resolve()
            if (!resolvedScreen) {
                PsiDirectory commonThemeDir = ph.getComponentDir('common-theme')
                if (!commonThemeDir) return
                XmlFile commonScreenFile = commonThemeDir.findSubdirectory('widget').findFile('CommonScreens.xml')
                ScreenFile screenFile = DomManager.getDomManager(myAttrValue.project)
                        .getFileElement(commonScreenFile, ScreenFile.class)
                        ?.rootElement
                resolvedScreen = screenFile.screens.find {
                    it.name.value == decoratorName
                }?.xmlElement
            }
            if (!resolvedScreen) return

            List<XmlTag> decoratorSectionTags = PsiTreeUtil.collectElements(resolvedScreen, getSectionTagFilter())
            return decoratorSectionTags.collect { it.getAttributeValue('name') }
        }?.flatten()

        decoratorSections.forEach {
            LookupElement lookupElement = LookupElementBuilder.create(it)
                    .withTailText(" Found in FooScreen" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }

    private static PsiElementFilter getSectionTagFilter() {
        new PsiElementFilter() {
            @Override
            boolean isAccepted(@NotNull PsiElement psiElement) {
                boolean isTag = psiElement instanceof XmlTag
                if (!isTag) return false
                return isTag && ((psiElement as XmlTag).name == 'decorator-section-include')
            }
        }
    }

}
