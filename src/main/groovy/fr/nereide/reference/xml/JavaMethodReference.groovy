package fr.nereide.reference.xml

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import org.jetbrains.annotations.Nullable

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class JavaMethodReference extends PsiReferenceBase<XmlElement> {

    private final PsiClass currentClass

    JavaMethodReference(XmlAttributeValue methodName, PsiClass currentClass, boolean soft) {
        super(methodName, soft)
        this.currentClass = currentClass
    }

    @Nullable
    PsiElement resolve() {
        return this.currentClass?.methods?.find { method -> method.name == this.value }
    }

}
