package fr.nereide.reference.xml

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import org.jetbrains.annotations.Nullable

class JavaMethodReference extends PsiReferenceBase<XmlElement> {
    private final PsiClass currentClass
    JavaMethodReference(XmlAttributeValue methodName, PsiClass currentClass, boolean soft) {
        super(methodName, soft)
        this.currentClass = currentClass
    }

    @Nullable
    PsiElement resolve() {
        PsiMethod[] methods = this.currentClass.getMethods();
        String val = this.getValue()

        for (PsiMethod method : methods) {
            if (method.getName().equals(val)) {
                return method
            }
        }
        return null
    }
}