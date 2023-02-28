package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.dom.FormFile
import org.jetbrains.annotations.Nullable

class GridReference extends GenericXmlReference {
    GridReference(XmlAttributeValue gridName, boolean soft) {
        super(gridName, soft,
                "getGrids",
                "getName",
                "getGrid",
                FormFile.class)
    }

    @Nullable
    PsiElement resolve() {
        super.resolve()
    }
}