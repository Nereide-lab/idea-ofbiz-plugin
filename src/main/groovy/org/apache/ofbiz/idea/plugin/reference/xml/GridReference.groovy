package org.apache.ofbiz.idea.plugin.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import org.jetbrains.annotations.Nullable

class GridReference extends GenericXmlReference {
    GridReference(XmlAttributeValue gridName, boolean soft) {
        super(gridName, soft,
                "getGrids",
                "getName",
                "getGrid",
                org.apache.ofbiz.idea.plugin.dom.FormFile.class)
    }

    @Nullable
    PsiElement resolve() {
        super.resolve()
    }
}