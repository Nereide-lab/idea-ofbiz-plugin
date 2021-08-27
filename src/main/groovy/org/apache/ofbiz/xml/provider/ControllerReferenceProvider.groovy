package org.apache.ofbiz.xml.provider

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.apache.ofbiz.xml.reference.ControllerReference
import org.jetbrains.annotations.NotNull

class ControllerReferenceProvider extends PsiReferenceProvider {
    ControllerReferenceProvider() {}

    private static final Logger LOG = Logger.getInstance(ControllerReferenceProvider.class);

    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            ControllerReference test = new ControllerReference((XmlAttributeValue) element, true);
            PsiReference[] reference = (PsiReference) test;
            return reference;
        }
        return  PsiReference.EMPTY_ARRAY;
    }
}

