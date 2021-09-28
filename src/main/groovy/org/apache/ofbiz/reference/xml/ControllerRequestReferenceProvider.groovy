package org.apache.ofbiz.reference.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class ControllerRequestReferenceProvider extends PsiReferenceProvider {
    ControllerRequestReferenceProvider() {}

    private static final Logger LOG = Logger.getInstance(ControllerRequestReferenceProvider.class)

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            ControllerRequestReference controller = new ControllerRequestReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) controller
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}