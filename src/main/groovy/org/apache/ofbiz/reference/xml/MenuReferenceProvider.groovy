package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class MenuReferenceProvider extends PsiReferenceProvider {
    MenuReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            MenuReference menu = new MenuReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) menu
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}
