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
import fr.nereide.dom.element.form.Form
import fr.nereide.dom.file.FormFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin completion system
 */
class FormNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    public static final String LOCATION_ATTR = 'location'
    /* codenarc-disable UnusedMethodParameter */

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {

        /* codenarc-enable UnusedMethodParameter */
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.position
        List<Form> forms
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag parentTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag)
        if (parentTag.getAttribute(LOCATION_ATTR)) {
            XmlAttributeValue menuLocationAttr = parentTag.getAttribute(LOCATION_ATTR).valueElement
            forms = ph.getDomElementListFromFileAtLocation(menuLocationAttr.value, FormFile)
        } else {
            forms = ph.collectAllFormsFromCurrentFileFromElement(myAttrValue)
        }

        forms.each { Form form ->
            LookupElement lookupElement = LookupElementBuilder.create(form.name.value as String)
                    .withTailText(" Component:${MiscUtils.getComponentName(form as Form)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement,
                    OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY))
        }
    }

}
