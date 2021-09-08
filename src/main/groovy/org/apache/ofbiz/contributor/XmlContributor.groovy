package org.apache.ofbiz.contributor

import com.intellij.patterns.XmlNamedElementPattern.XmlAttributePattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import org.apache.ofbiz.reference.xml.ControllerReferenceProvider
import org.apache.ofbiz.reference.xml.EntityReferenceProvider
import org.jetbrains.annotations.NotNull

class XmlContributor extends PsiReferenceContributor {
    XmlContributor() {}

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FORM_TARGET_PATTERN), new ControllerReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(ENTITY_NAME_ATTR_PATTERN), new EntityReferenceProvider()
        )
    }

    // =============================================================
    //                      PATTERNS
    // =============================================================

    public static final XmlAttributePattern FORM_TARGET_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("target")

    public static final XmlAttributePattern ENTITY_NAME_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity", "entity-name", "default-entity-name", "rel-entity-name")
}
