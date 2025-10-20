package fr.nereide.reference.xml

import com.intellij.psi.PsiClass
import com.intellij.psi.xml.XmlAttributeValue

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class GroovyServiceDefReference extends JavaMethodReference {

    GroovyServiceDefReference(XmlAttributeValue methodName, PsiClass currentClass, boolean soft) {
        super(methodName, currentClass, soft)
    }

}
