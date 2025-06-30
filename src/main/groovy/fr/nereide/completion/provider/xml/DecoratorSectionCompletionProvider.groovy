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
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

import static com.intellij.util.xml.DomManager.getDomManager

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
        Screen decoratorScreen = getDecoratorScreenFromContext(ph, myAttrValue)
        if (!decoratorScreen) return
        List<IncludeScreen> includes = getIncludesInScreenDom(decoratorScreen)

        List<XmlAttribute> decoratorSections = includes.collect { IncludeScreen incScrTag ->
            getDecoratorSectionsFromInclude(incScrTag, ph, decoratorScreen.name.value)
        }
                ?.flatten()
                ?.findAll { it != null }

        decoratorSections.forEach { XmlAttribute attr ->
            String componentName = MiscUtils.getComponentName(attr)
            String fileName = attr.getContainingFile().getName()
            LookupElement lookupElement = LookupElementBuilder.create(attr.value)
                    .withTailText("  $fileName [$componentName]" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }

    static Screen getDecoratorScreenFromContext(OfbizProjectHelper ph, XmlElement myAttrValue) {
        XmlTag decoratorSectionTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag.class, false)
        int i = 0
        while (decoratorSectionTag.localName != 'decorator-section' && i < 4) {
            decoratorSectionTag = PsiTreeUtil.getParentOfType(decoratorSectionTag, XmlTag.class, false)
            i++
        }
        if (decoratorSectionTag.localName != 'decorator-section') return
        XmlTag decoratorScreenTag = decoratorSectionTag.parentTag
        i = 0
        while (decoratorSectionTag.parentTag && decoratorScreenTag.localName != 'decorator-screen' && i < 4) {
            decoratorScreenTag = decoratorSectionTag.parentTag
            i++
        }
        if (decoratorScreenTag.localName != 'decorator-screen') return
        String screenLocation = decoratorScreenTag.getAttributeValue('location')
        String decoratorName = decoratorScreenTag.getAttributeValue('name')
        return ph.getScreenFromFileAtLocation(screenLocation, decoratorName)
    }

    private static List<XmlAttribute> getDecoratorSectionsFromInclude(IncludeScreen incScreenDom, OfbizProjectHelper ph, String decoratorName) {
        List result = []
        XmlAttribute screenNameXmlValue = incScreenDom.name.xmlElement as XmlAttribute
        XmlTag resolvedScreen = new ScreenReference(screenNameXmlValue.valueElement).resolve() as XmlTag
        if (!resolvedScreen) {
            PsiDirectory commonThemeDir = ph.getComponentDir('common-theme')
            if (!commonThemeDir) return
            XmlFile commonScreenFile = commonThemeDir.findSubdirectory('widget').findFile('CommonScreens.xml') as XmlFile
            ScreenFile screenFile = getDomManager(ph.getProject())
                    .getFileElement(commonScreenFile, ScreenFile.class)
                    ?.rootElement
            resolvedScreen = screenFile.screens.find {
                it.name.value == decoratorName
            }?.xmlElement as XmlTag
        }
        if (!resolvedScreen) return

        List<IncludeScreen> includes = getIncludesInScreenDom(
                getDomManager(ph.getProject()).getDomElement(resolvedScreen) as Screen)

        if (includes) {
            includes.forEach { include ->
                result << getDecoratorSectionsFromInclude(include, ph, include.name.value)
            }
        }
        result << PsiTreeUtil.collectElements(resolvedScreen, getSectionTagFilter())
                ?.collect { XmlTag sectionIncludeTag -> sectionIncludeTag.getAttribute('name') }
        return result
    }

    private static PsiElementFilter getSectionTagFilter() {
        new PsiElementFilter() {
            @Override
            boolean isAccepted(@NotNull PsiElement psiElement) {
                boolean isTag = psiElement instanceof XmlTag
                if (!isTag) return false
                return isTag && ((psiElement as XmlTag).localName == 'decorator-section-include')
            }
        }
    }

    private static List<IncludeScreen> getIncludesInScreenDom(Screen decoratorScreen) {
        return decoratorScreen.sections?.flatten()
                ?.collect { ScreenSection scrSec -> scrSec.widgets }?.flatten()
                ?.collect { ScreenWidget scrWid -> scrWid.includeScreens }?.flatten()
    }
}
