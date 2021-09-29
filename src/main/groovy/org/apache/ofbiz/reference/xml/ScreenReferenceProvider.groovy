package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

import java.util.regex.Matcher
import java.util.regex.Pattern

class ScreenReferenceProvider extends PsiReferenceProvider {
    ScreenReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            ScreenReference screen = new ScreenReference((XmlAttributeValue) element, true)
            PsiReference[] reference = (PsiReference) screen
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}
