package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import org.apache.ofbiz.dom.FormFile
import org.jetbrains.annotations.Nullable

class FormReference extends GenericXmlReference {

    FormReference(XmlAttributeValue formName, boolean soft) {
        super(formName, soft,
                "getForms",
                "getName",
                "getForm",
                FormFile.class)
    }

    @Nullable
    PsiElement resolve() {
        super.resolve()
    }
}