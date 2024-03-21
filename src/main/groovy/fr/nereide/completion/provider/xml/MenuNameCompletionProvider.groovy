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
import com.intellij.util.xml.DomManager
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

import static fr.nereide.dom.MenuFile.Menu

class MenuNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        ProjectServiceInterface ps = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        PsiElement myElement = parameters.getPosition()
        List<Menu> menus
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        XmlTag parentTag = PsiTreeUtil.getParentOfType(myAttrValue, XmlTag.class)
        if (parentTag.getAttribute('location')) {
            XmlAttributeValue menuLocationAttr = parentTag.getAttribute('location').getValueElement()
            menus = ps.getMenuListFromFileAtLocation(DomManager.getDomManager(menuLocationAttr.getProject()),
                    menuLocationAttr.getValue())
        } else {
            menus = []
        }

        menus.each { Menu menu ->
            LookupElement lookupElement = LookupElementBuilder.create(menu.getName().getValue() as String)
                    .withTailText(" Component:${MiscUtils.getComponentName(menu as Menu)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}