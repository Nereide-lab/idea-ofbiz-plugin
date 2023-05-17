package fr.nereide.completion.provider.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import fr.nereide.completion.provider.common.EntityFieldCompletionProvider

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class XmlEntityFieldsCompletionProvider extends EntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement psiElement) {
        String entityAlias = null
        XmlTag initialContainingTag = getParentOfType(psiElement, XmlTag.class)
        XmlTag fullView = null

        switch (initialContainingTag.getName()) {
            case 'alias':
                entityAlias = initialContainingTag.getAttribute('entity-alias')?.getValue()
                if (!entityAlias) return null
                fullView = getParentOfType(initialContainingTag, XmlTag.class)
                break
            case 'key-map':
                XmlTag viewLinkTag = getParentOfType(initialContainingTag, XmlTag.class)
                entityAlias = getParentOfType(psiElement, XmlAttribute.class).getName() == 'field-name' ?
                        viewLinkTag.getAttributeValue('entity-alias') :
                        viewLinkTag.getAttributeValue('rel-entity-alias')
                fullView = getParentOfType(viewLinkTag, XmlTag.class)
                break
            default: // cas du fichier de chargement de données
                return initialContainingTag.getName()

        }
        if (!entityAlias || !fullView) return null

        Optional<XmlTag> relevantMember = List.of(fullView.getSubTags()).stream()
                .filter { it.getName() == 'member-entity' }
                .filter { it.getAttribute('entity-alias')?.getValue() == entityAlias }
                .findFirst()
        return relevantMember.isEmpty() ? null :
                relevantMember.get().getAttribute('entity-name')?.getValue()
    }

    Class getAssigmentClass() {
        return null
    }

    String getAssigmentString(PsiElement assign) {
        return null
    }
}