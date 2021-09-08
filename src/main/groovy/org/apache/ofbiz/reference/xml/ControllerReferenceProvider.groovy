package org.apache.ofbiz.reference.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class ControllerReferenceProvider extends PsiReferenceProvider {
    ControllerReferenceProvider() {}

    private static final Logger LOG = Logger.getInstance(ControllerReferenceProvider.class)

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            ControllerReference controller = new ControllerReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) controller
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}