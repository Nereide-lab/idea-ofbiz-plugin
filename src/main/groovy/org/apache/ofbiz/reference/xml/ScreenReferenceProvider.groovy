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
    //regex qui récupère tout ce qu'il y a derrière le '#', donc le nom de l'écran
    static final Pattern SCREEN_NAME_PATTERN = Pattern.compile("[^#]*\$")

    ScreenReferenceProvider() {}

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (element instanceof XmlAttributeValue) {
            Matcher matcher = SCREEN_NAME_PATTERN.matcher(element.getValue())
            String screenName = matcher.find() ? matcher.group(0) : element.getValue()
            ScreenReference screen = new ScreenReference((XmlAttributeValue) element,screenName, true)
            PsiReference[] reference = (PsiReference) screen
            return reference
        }
        return PsiReference.EMPTY_ARRAY
    }
}
