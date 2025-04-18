package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.entitymodel.ViewEntity
import org.jetbrains.annotations.NotNull

class EntityAliasCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {

        XmlElement myElement
        try {
            myElement = parameters.getPosition() as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myElement = parameters.getPosition() as XmlElement
        }
        XmlTag tag = PsiTreeUtil.getParentOfType(myElement, XmlTag.class)
        if (!('view-entity' == tag.getName())) {
            tag = PsiTreeUtil.getParentOfType(tag, XmlTag.class)
            if (!('view-entity' == tag.getName())) {
                return
            }
        }
        DomManager dm = DomManager.getDomManager(parameters.position.project)
        ViewEntity myView = dm.getDomElement(tag) as ViewEntity
        myView.getMemberEntities().forEach { viewMem ->
            LookupElement lookupElement = LookupElementBuilder.create(viewMem.entityAlias.value)
                    .withTailText(" ${viewMem.entityName.value}", true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}
