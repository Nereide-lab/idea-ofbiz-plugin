package fr.nereide.reference.xml.provider

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.nereide.reference.xml.JavaMethodReference
import org.jetbrains.annotations.NotNull

class JavaMethodReferenceProvider extends PsiReferenceProvider {
    JavaMethodReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (FileReferenceProvider.isJavaService(element)) {
            String classLocation = getClassLocation(element)
            if (!classLocation) return PsiReference.EMPTY_ARRAY
            PsiClass aClass = JavaPsiFacade.getInstance(element.getProject())
                    .findClass(classLocation, GlobalSearchScope.allScope(element.getProject()))
            return (PsiReference) new JavaMethodReference((XmlAttributeValue) element, aClass, true)
        }
        return PsiReference.EMPTY_ARRAY
    }

    static String getClassLocation(PsiElement element) {
        XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class)
        XmlAttribute locationAttr = tag.getAttribute("location")
        if (!locationAttr) locationAttr = tag.getAttribute("path")
        return locationAttr ? locationAttr.getValue() : null
    }
}
