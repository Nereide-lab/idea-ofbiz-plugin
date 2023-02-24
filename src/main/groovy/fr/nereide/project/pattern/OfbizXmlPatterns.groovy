package fr.nereide.project.pattern


import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlAttributeValuePattern
import fr.nereide.dom.CompoundFileDescription

import static com.intellij.patterns.PlatformPatterns.psiElement
import static com.intellij.patterns.StandardPatterns.string
import static com.intellij.patterns.XmlPatterns.xmlAttribute
import static com.intellij.patterns.XmlPatterns.xmlAttributeValue
import static com.intellij.patterns.XmlPatterns.xmlTag

class OfbizXmlPatterns {
    public static final XmlAttributeValuePattern URI_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue()
                    .inside(xmlAttribute()
                            .withName("target")
                            .inside(xmlTag().withName("form"))
                    ),
            xmlAttributeValue()
                    .inside(xmlAttribute()
                            .withName("target")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.FORM_NS_PREFIX}form")
                                    .withNamespace(CompoundFileDescription.FORM_NS_URL)
                            )
                    )
    )
    public static final XmlAttributeValuePattern RESPONSE_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("value")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SITE_CONF_NS_PREFIX}response")
                                    .withNamespace(CompoundFileDescription.SITE_CONF_NS_URL))),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("value")
                            .inside(xmlTag()
                                    .withName("response"))
            )
    )
    public static final XmlAttributeValuePattern ENTITY_CALL = xmlAttributeValue()
            .inside(xmlAttribute()
                    .withName("entity", "entity-name", "default-entity-name", "rel-entity-name"))
    public static final XmlAttributeValuePattern SERVICE_DEF_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("service", "service-name")),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("event")
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().contains("service"))))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SITE_CONF_NS_PREFIX}event")
                                    .withNamespace(CompoundFileDescription.SITE_CONF_NS_URL)
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().contains("service")))
                            )
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName('name')
                            .inside(xmlTag()
                                    .withName('invoke')
                                    .inside(xmlTag()
                                            .withName('group')
                                            .inside(xmlTag()
                                                    .withName('service')
                                                    .withAttributeValue('engine', 'group')
                                            )
                                    )
                            )
            )
    )
    public static final XmlAttributeValuePattern JAVA_EVENT_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("event")
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().equalTo("java")))
                            )),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SITE_CONF_NS_PREFIX}event")
                                    .withNamespace(CompoundFileDescription.SITE_CONF_NS_URL)
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().equalTo("java")))
                            )),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("service")
                                    .withChild(xmlAttribute()
                                            .withName("engine")
                                            .withValue(string().equalTo("java")))
                            ))
    )
    public static final XmlAttributeValuePattern LABEL_CALL = xmlAttributeValue().inside(
            xmlAttribute().withValue(string().startsWith('${uiLabelMap.'))
    )
    public static final XmlAttributeValuePattern FORM_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .inside(xmlTag().withName("include-form"))
                            .withName("name"),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .inside(xmlTag().withName("form"))
                            .withName("extends"),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.FORM_NS_PREFIX}include-form")
                                    .withNamespace(CompoundFileDescription.FORM_NS_URL)
                            ),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SCREEN_NS_PREFIX}include-form")
                                    .withNamespace(CompoundFileDescription.SCREEN_NS_URL)
                            ),
            ),
    )
    public static final XmlAttributeValuePattern GRID_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .inside(xmlTag().withName("include-grid"))
                            .withName("name"),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .inside(xmlTag().withName("grid"))
                            .withName("extends"),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.FORM_NS_PREFIX}include-grid")
                                    .withNamespace(CompoundFileDescription.FORM_NS_URL)
                            ),
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SCREEN_NS_PREFIX}include-grid")
                                    .withNamespace(CompoundFileDescription.SCREEN_NS_URL)
                            ),
            )
    )
    public static final XmlAttributeValuePattern MENU_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("navigation-menu-name")
                            .inside(xmlTag().withName("screenlet"))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag().withName("include-menu"))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SCREEN_NS_PREFIX}include-menu")
                                    .withNamespace(CompoundFileDescription.SCREEN_NS_URL)
                            )
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.FORM_NS_PREFIX}include-menu")
                                    .withNamespace(CompoundFileDescription.FORM_NS_URL)
                            )
            )
    )
    public static final XmlAttributeValuePattern FILE_CALL = xmlAttributeValue()
            .inside(xmlAttribute()
                    .withName("entity-xml-url", "xml-resource", "extends-resource",
                            "resourceValue", "resource", "template", "page", "location", "image-location",
                            "component-location", "fallback-location", "default-fallback-location",
                            "default-location", "path")
            )
    public static final XmlAttributeValuePattern SCREEN_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag().withName("include-screen"))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("page")
                            .inside(xmlTag()
                                    .withName("view-map")
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().equalTo("screen"))))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("page")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SITE_CONF_NS_PREFIX}view-map")
                                    .withNamespace(CompoundFileDescription.SITE_CONF_NS_URL)
                                    .withChild(xmlAttribute()
                                            .withName("type")
                                            .withValue(string().equalTo("screen"))))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SCREEN_NS_PREFIX}include-screen")
                                    .withNamespace(CompoundFileDescription.SCREEN_NS_URL)
                            )
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.FORM_NS_PREFIX}include-screen")
                                    .withNamespace(CompoundFileDescription.FORM_NS_URL)
                            )
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .withParent(xmlTag().withName("decorator-screen"))
            ),
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("name")
                            .inside(xmlTag()
                                    .withName("${CompoundFileDescription.SCREEN_NS_PREFIX}decorator-screen")
                                    .withNamespace(CompoundFileDescription.SCREEN_NS_URL)
                            )
            )
    )
    public static final XmlAttributeValuePattern GROOVY_SERVICE_METHOD = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(
                    xmlAttribute()
                            .withName("invoke")
                            .inside(xmlTag()
                                    .withName("service")
                                    .withChild(
                                            xmlAttribute().withName("engine").withValue(string().contains("groovy"))
                                    )
                            )
            )
    )
    public static final XmlAttributeValuePattern DATASOURCE_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(xmlAttribute().withName("datasource-name"))
    )
    public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement()
            .inside(ENTITY_CALL)
    public static final PsiElementPattern SERVICE_DEF_CALL_COMPL = psiElement()
            .inside(SERVICE_DEF_CALL)
}
