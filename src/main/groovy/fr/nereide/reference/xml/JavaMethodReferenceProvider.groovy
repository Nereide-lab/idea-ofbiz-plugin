package fr.nereide.reference.xml

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class JavaMethodReferenceProvider extends PsiReferenceProvider {
    JavaMethodReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (FileReferenceProvider.isJavaService(element)) {
            XmlAttribute parent = (XmlAttribute) element.getParent();
            String className = parent.getParent().getAttributeValue("location")
            if (!className) {
                className = parent.getParent().getAttributeValue("path")
            }
            if (!className) {
                return PsiReference.EMPTY_ARRAY
            }

            PsiClass currentClass = JavaPsiFacade.getInstance(element.getProject())
                    .findClass(className, GlobalSearchScope.allScope(element.getProject()))

            if (currentClass != null) {
                PsiMethod[] currentClassMethods = currentClass.getAllMethods()
                for (PsiMethod method : currentClassMethods) {
                    if (method.getName() == element.getValue()) {
                        return (PsiReference) new JavaMethodReference((XmlAttributeValue) element, currentClass, true)
                    }
                }
            }
        }
        return PsiReference.EMPTY_ARRAY
    }
}
