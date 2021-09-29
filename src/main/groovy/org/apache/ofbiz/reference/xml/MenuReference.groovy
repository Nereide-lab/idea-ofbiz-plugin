package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import org.apache.ofbiz.dom.MenuFile
import org.jetbrains.annotations.Nullable

class MenuReference extends GenericXmlReference {


    MenuReference(XmlAttributeValue menuName, boolean soft) {
        super(menuName, soft,
                "getMenus",
                "getName",
                "getMenu",
                MenuFile.class)
    }

    @Nullable
    PsiElement resolve() {
        super.resolve()
    }

}