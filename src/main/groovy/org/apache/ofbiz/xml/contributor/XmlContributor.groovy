package org.apache.ofbiz.xml.contributor

import com.intellij.patterns.XmlNamedElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import org.apache.ofbiz.xml.provider.ControllerReferenceProvider
import org.jetbrains.annotations.NotNull

class XmlContributor extends PsiReferenceContributor{
    public XmlContributor() {}

    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FORM_TARGET_PATTERN), new ControllerReferenceProvider()
        );
    }

    // Patterns
    public static final XmlNamedElementPattern.XmlAttributePattern FORM_TARGET_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("target");
}
