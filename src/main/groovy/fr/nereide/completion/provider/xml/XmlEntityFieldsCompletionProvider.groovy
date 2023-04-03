package fr.nereide.completion.provider.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class XmlEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement psiElement) {
        XmlTag aliasTag = getParentOfType(psiElement, XmlTag.class)
        String entityAlias = aliasTag.getAttribute('entity-alias')?.getValue()
        if (!entityAlias) return null

        XmlTag fullView = getParentOfType(aliasTag, XmlTag.class)

        Optional<XmlTag> relevantMember = List.of(fullView.getSubTags()).stream()
                .filter { it.getName() == 'member-entity' }
                .filter { it.getAttribute('entity-alias')?.getValue() == entityAlias }
                .findFirst()
        return relevantMember.isEmpty() ? null :
                relevantMember.get().getAttribute('entity-name')?.getValue()
    }
}