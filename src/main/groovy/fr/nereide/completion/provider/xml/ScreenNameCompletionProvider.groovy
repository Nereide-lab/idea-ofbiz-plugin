package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.nereide.dom.element.screen.Screen
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.utils.XmlUtils
import org.jetbrains.annotations.NotNull


class ScreenNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (!PluginActivator.getInstance(parameters.position.project).isActive()) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.getPosition()
        List<Screen> screens
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag parentTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag.class)
        if (parentTag.getAttribute('location')) {
            XmlAttributeValue screenLocationAttr = parentTag.getAttribute('location').getValueElement()
            screens = ph.getScreensFromScreenFileAtLocation(screenLocationAttr, false)
        } else if (XmlUtils.isPageReferenceFromController(parentTag)) {
            screens = ph.getScreensFromScreenFileAtLocation(myAttrValue, true)
        } else {
            screens = ph.getAllScreenFromCurrentFileFromElement(myAttrValue)
        }

        screens.each { Screen screen ->
            LookupElement lookupElement = LookupElementBuilder.create(screen.getName().getValue() as String)
                    .withTailText(" Component:${MiscUtils.getComponentName(screen as Screen)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}
