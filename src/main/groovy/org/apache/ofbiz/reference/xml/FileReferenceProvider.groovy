package org.apache.ofbiz.reference.xml


import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.converters.ClassValueConverterImpl
import org.apache.ofbiz.reference.common.ComponentAwareFileReferenceSet
import org.jetbrains.annotations.NotNull

class FileReferenceProvider extends PsiReferenceProvider {
    FileReferenceProvider() {}

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (isJavaService(element)) {
            return ClassValueConverterImpl.getClassValueConverter().createReferences(null, element, null)
        } else {
            return ComponentAwareFileReferenceSet.createSet(element, true, true, false)
                    .getAllReferences()
        }
    }

    static boolean isJavaService(PsiElement element) {
        if (element.getParent() instanceof XmlAttribute) {
            XmlAttribute parent = (XmlAttribute) element.getParent()
            if (parent.getParent().getAttributeValue("engine") != null && parent.getParent()
                    .getAttributeValue("engine")
                    .equalsIgnoreCase("java")) {
                return true
            }
        }
        return false
    }
}
