package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.FormFile

class GridReference extends GenericXmlReference {

    Class fileType = FormFile.class

    GridReference(XmlAttributeValue gridName, boolean soft) {
        super(gridName, soft)
    }

    PsiElement resolve() {
        XmlTag containingTag = (XmlTag) getTag(this.getElement())
        if (!containingTag) {
            return null
        }
        PsiElement locationAttribute = containingTag.getAttribute('location')
        if (locationAttribute) {
            String locationAttributeValue = locationAttribute.getValue()
            return ps.getGridFromFileAtLocation(dm, locationAttributeValue, this.getValue()).getXmlElement()
        } else if (isInRightFile(this.getElement(), fileType, dm)) {
            PsiFile currentFile = this.getElement().getContainingFile()
            return ps.getGridFromPsiFile(dm, currentFile, this.getElement().getValue()).getXmlElement()
        }
        return null
    }
}
