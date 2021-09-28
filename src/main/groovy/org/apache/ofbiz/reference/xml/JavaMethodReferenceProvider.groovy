package org.apache.ofbiz.reference.xml

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class JavaMethodReferenceProvider extends PsiReferenceProvider {
    JavaMethodReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        /*if (element instanceof XmlAttributeValue) {
            JavaMethodReference method = new JavaMethodReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) method
            return reference
        }
        return PsiReference.EMPTY_ARRAY

         */
        PsiReference[] results = null;
        if (OfbizReferenceUtils.isJavaService(element)) {
            XmlAttribute parent = (XmlAttribute) element.getParent();
            if (parent.getParent().getAttributeValue("location") == null) {
                LOG.debug("Error in JavaMethodReferenceProvider, parent element has no location")
                return null;
            }

            String className = parent.getParent().getAttributeValue("location")
            Project project = element.getProject()
            PsiClass currentClass = JavaPsiFacade.getInstance(project)
                    .findClass(className, GlobalSearchScope.allScope(project))

            if (currentClass != null) {
                PsiMethod[] currentClassMethods = currentClass.getAllMethods()
                for (PsiMethod method : currentClassMethods) {
                    if (method.getName().equals(((XmlAttributeValueImpl) element).getValue())) {
                        results = (PsiReference) new JavaMethodReference((XmlAttributeValue) element, true)
                    }
                }
                return results;
            }
        }

        return results = PsiReference.EMPTY_ARRAY;

    }
}
