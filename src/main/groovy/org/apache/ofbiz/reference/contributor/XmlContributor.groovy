package org.apache.ofbiz.reference.contributor


import com.intellij.patterns.XmlNamedElementPattern.XmlAttributePattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import org.apache.ofbiz.reference.xml.ControllerReferenceProvider
import org.apache.ofbiz.reference.xml.EntityReferenceProvider
import org.apache.ofbiz.reference.xml.FileReferenceProvider
import org.apache.ofbiz.reference.xml.FormReferenceProvider
import org.apache.ofbiz.reference.xml.ServiceReferenceProvider
import org.apache.ofbiz.reference.xml.UiLabelReferenceProvider
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
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(SERVICE_NAME_ATTR_PATTERN), new ServiceReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(PROPERTY_ATTR_PATTERN), new UiLabelReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FORM_LOCATION_PATTERN), new FormReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FILE_LOCATION_ATTR_PATTERN), new FileReferenceProvider()
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

    public static final XmlAttributePattern SERVICE_NAME_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("service", "service-name")

    public static final XmlAttributePattern PROPERTY_ATTR_PATTERN =
            XmlPatterns.xmlAttribute().withValue(
                    XmlPatterns.string().startsWith('${uiLabelMap.'))

    public static final XmlAttributePattern FORM_LOCATION_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-form"))
                    .withName("name"),

            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-grid"))
                    .withName("name"),

            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("extends"),

            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("grid"))
                    .withName("extends")
    )

    public static final XmlAttributePattern FILE_LOCATION_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity-xml-url", "xml-resource", "extends-resource",
                            "resourceValue", "resource", "template", "page", "location", "image-location",
                            "component-location", "fallback-location", "default-fallback-location",
                            "default-location", "path")

}
