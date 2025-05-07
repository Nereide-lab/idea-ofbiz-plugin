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
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

class FormNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (!PluginActivator.getInstance(parameters.position.project).isActive()) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        PsiElement myElement = parameters.getPosition()
        List<Form> forms
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag parentTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag.class)
        if (parentTag.getAttribute('location')) {
            XmlAttributeValue menuLocationAttr = parentTag.getAttribute('location').getValueElement()
            forms = ph.getDomElementListFromFileAtLocation(menuLocationAttr.value, FormFile.class)
        } else {
            forms = ph.getAllFormsFromCurrentFileFromElement(myAttrValue)
        }

        forms.each { Form form ->
            LookupElement lookupElement = LookupElementBuilder.create(form.getName().getValue() as String)
                    .withTailText(" Component:${MiscUtils.getComponentName(form as Form)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}