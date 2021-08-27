package org.apache.ofbiz.xml.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import org.jetbrains.annotations.Nullable

public class ControllerReference extends PsiReferenceBase<XmlAttributeValue> {
    public ControllerReference(XmlAttributeValue element, boolean soft) {
        super(element, soft);
    }

    @Nullable
    public PsiElement resolve() {
        return this.getElement();
    }
}