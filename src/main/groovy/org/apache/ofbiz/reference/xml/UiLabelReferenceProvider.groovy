package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class UiLabelReferenceProvider extends PsiReferenceProvider {

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            UiLabelReference property = new UiLabelReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) property
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}
