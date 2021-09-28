package org.apache.ofbiz.reference.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class ControllerViewReferenceProvider extends PsiReferenceProvider {
    ControllerViewReferenceProvider() {}

    private static final Logger LOG = Logger.getInstance(ControllerViewReferenceProvider.class)

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            ControllerViewReference controller = new ControllerViewReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) controller
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}