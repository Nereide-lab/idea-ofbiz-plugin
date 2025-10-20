package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.file.FormFile
import fr.nereide.project.utils.XmlUtils

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class GridReference extends GenericXmlReference {

    Class fileType = FormFile

    GridReference(XmlAttributeValue gridName, boolean soft) {
        super(gridName, soft)
    }

    PsiElement resolve() {
        XmlTag containingTag = (XmlTag) XmlUtils.getParentTag(this.element)
        if (!containingTag) {
            return null
        }
        PsiElement locationAttribute = containingTag.getAttribute('location')
        if (locationAttribute) {
            String locationAttributeValue = locationAttribute.value
            return ph.getGridFromFileAtLocation(locationAttributeValue, this.value).xmlElement
        } else if (XmlUtils.isInRightFile(this.element, fileType, dm)) {
            PsiFile currentFile = this.element.containingFile
            return ph.getGridFromPsiFile(currentFile, this.element.value).xmlElement
        }
        return null
    }

}
