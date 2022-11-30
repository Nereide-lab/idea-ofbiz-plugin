package fr.nereide.reference.xml

import com.intellij.psi.PsiClass
import com.intellij.psi.xml.XmlAttributeValue

class GroovyServiceDefReference extends JavaMethodReference {
    GroovyServiceDefReference(XmlAttributeValue methodName, PsiClass currentClass, boolean soft) {
        super(methodName, currentClass, soft)
    }
}
