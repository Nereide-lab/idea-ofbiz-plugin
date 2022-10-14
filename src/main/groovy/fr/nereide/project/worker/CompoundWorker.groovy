package fr.nereide.project.worker

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import fr.nereide.dom.FormFile

class CompoundWorker {

    static List<DomElement> getDomElementListFromCompound(DomFileElement<DomElement> domFile, String listGetterMethod, Class fileType) {
        List<DomElement> toReturn = []
        if (fileType.isAssignableFrom(FormFile.class)) {
            List elements = domFile.getRootElement().getForms().getForms()
            if (elements) toReturn.addAll(elements)
            elements = domFile.getRootElement().getForms().getGrids()
            if (elements) toReturn.addAll(elements)
        } else {
            toReturn.addAll(domFile.getRootElement()."$listGetterMethod"()."$listGetterMethod"())
        }
        toReturn
    }
}
