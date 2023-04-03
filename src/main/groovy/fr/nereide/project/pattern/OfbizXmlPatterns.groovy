package fr.nereide.project.pattern

import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlAttributeValuePattern

import static com.intellij.patterns.PlatformPatterns.psiElement
import static com.intellij.patterns.StandardPatterns.string
import static com.intellij.patterns.XmlNamedElementPattern.XmlAttributePattern
import static com.intellij.patterns.XmlPatterns.xmlAttribute
import static com.intellij.patterns.XmlPatterns.xmlAttributeValue
import static com.intellij.patterns.XmlPatterns.xmlTag
import static com.intellij.patterns.XmlTagPattern.Capture
import static fr.nereide.dom.CompoundFileDescription.FORM_NS_PREFIX
import static fr.nereide.dom.CompoundFileDescription.FORM_NS_URL
import static fr.nereide.dom.CompoundFileDescription.SCREEN_NS_PREFIX
import static fr.nereide.dom.CompoundFileDescription.SCREEN_NS_URL
import static fr.nereide.dom.CompoundFileDescription.SITE_CONF_NS_PREFIX
import static fr.nereide.dom.CompoundFileDescription.SITE_CONF_NS_URL

class OfbizXmlPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final XmlAttributeValuePattern URI_CALL = xmlAttributeValue().withParent(
            targetAttr().withParent(xmlTag().andOr(formTag(), formTagWithFormNs()))
    )

    public static final XmlAttributeValuePattern RESPONSE_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(valueAttr().withParent(responseTagInSiteConfNs())),
            xmlAttributeValue().withParent(valueAttr().withParent(responseTag()))
    )

    public static final XmlAttributeValuePattern ENTITY_CALL = xmlAttributeValue()
            .withParent(xmlAttribute().withName('entity', 'entity-name', 'default-entity-name', 'rel-entity-name'))

    public static final XmlAttributeValuePattern SERVICE_DEF_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(serviceLikeAttr()),
            xmlAttributeValue().withParent(invokeAttr().withParent(eventTag().withChild(serviceTypeAttrValue()))),
            xmlAttributeValue().withParent(invokeAttr().withParent(eventTagInSiteConfNs().withChild(serviceTypeAttrValue()))),
            xmlAttributeValue().withParent(nameAttr().withParent(
                    invokeTag().withParent(groupTag().withParent(serviceTag().withChild(groupEngineAttrValue())))))
    )

    public static final XmlAttributeValuePattern JAVA_EVENT_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(invokeAttr().withParent(eventTag().withChild(javaTypeAttrValue()))),
            xmlAttributeValue().withParent(invokeAttr().withParent(eventTagInSiteConfNs().withChild(javaTypeAttrValue()))),
            xmlAttributeValue().withParent(invokeAttr().withParent(serviceTag().withChild(javaEngineAttrValue())))
    )

    public static final XmlAttributeValuePattern LABEL_CALL = xmlAttributeValue().withParent(
            xmlAttribute().withValue(string().startsWith('${uiLabelMap.'))
    )
    public static final XmlAttributeValuePattern FORM_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(nameAttr().withParent(includeFormTag())),
            xmlAttributeValue().withParent(extendsAttr().withParent(formTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeFormTagInFormNs())),
            xmlAttributeValue().withParent(nameAttr().withParent(formTagInScreenNs())),
    )

    public static final XmlAttributeValuePattern GRID_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(nameAttr().withParent(includeGridTag())),
            xmlAttributeValue().withParent(extendsAttr().withParent(gridTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(gridTagInFormNs())),
            xmlAttributeValue().withParent(nameAttr().withParent(gridTagInScreenNs()))
    )

    public static final XmlAttributeValuePattern MENU_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(navigationMenuNameAttr().withParent(screenletTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeMenuTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeMenuInScreenNs())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeMenuInFormNs())
            )
    )

    public static final XmlAttributeValuePattern FILE_CALL = xmlAttributeValue().withParent(
            xmlAttribute().withName('entity-xml-url', 'xml-resource', 'extends-resource',
                    'resourceValue', 'resource', 'template', 'page', 'location', 'image-location',
                    'component-location', 'fallback-location', 'default-fallback-location',
                    'default-location', 'path')
    )
    public static final XmlAttributeValuePattern SCREEN_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(pageAttr().withParent(viewMapTag().withChild(typeScreenAttrValue()))),
            xmlAttributeValue().withParent(pageAttr().withParent(viewMapInSiteConfNs().withChild(typeScreenAttrValue()))),
            xmlAttributeValue().withParent(nameAttr().withParent(decoratorScreenTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeScreenTag())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeScreenInScreenNs())),
            xmlAttributeValue().withParent(nameAttr().withParent(includeScreenInFormNs())),
            xmlAttributeValue().withParent(nameAttr().withParent(decoratorScreenTagInScreenNs()))
    )

    public static final XmlAttributeValuePattern GROOVY_SERVICE_METHOD = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(invokeAttr().withParent(serviceTag().withChild(groovyEngineAttrValue())))
    )

    public static final XmlAttributeValuePattern DATASOURCE_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().withParent(xmlAttribute().withName('datasource-name'))
    )

    public static final XmlAttributeValuePattern ENTITY_FIELD_CALL = xmlAttributeValue().andOr(
            xmlAttributeValue().inside(nameAttr().withParent(aliasTag()))
                    .andNot(xmlAttributeValue().inside(nameAttr().withParent(aliasTag().withChild(fieldAttr())))),
            xmlAttributeValue().inside(fieldAttr().withParent(aliasTag()))
    )


    public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement().inside(ENTITY_CALL)
    public static final PsiElementPattern SERVICE_DEF_CALL_COMPL = psiElement().inside(SERVICE_DEF_CALL)
    public static final PsiElementPattern ENTITY_FIELD_COMPL = psiElement().inside(ENTITY_FIELD_CALL)

    //============================================
    //       UTILITY METHODS
    //============================================
    static XmlAttributePattern makeAttrPattern(String attrName) {
        return xmlAttribute().withName(attrName)
    }

    static XmlAttributePattern makeAttrAndValPattern(String attrName, String attrVal) {
        return xmlAttribute().withName(attrName).withValue(string().contains(attrVal))
    }

    static Capture makeTagPattern(String tagName) {
        return xmlTag().withName(tagName)
    }

    static XmlAttributePattern pageAttr() { return makeAttrPattern('page') }

    static XmlAttributePattern navigationMenuNameAttr() { return makeAttrPattern('navigation-menu-name') }

    static XmlAttributePattern extendsAttr() { return makeAttrPattern('extends') }

    static XmlAttributePattern valueAttr() { return makeAttrPattern('value') }

    static XmlAttributePattern targetAttr() { return makeAttrPattern('target') }

    static XmlAttributePattern nameAttr() { return makeAttrPattern('name') }

    static XmlAttributePattern invokeAttr() { return makeAttrPattern('invoke') }

    static XmlAttributePattern fieldAttr() { xmlAttribute('field') }

    static XmlAttributePattern serviceLikeAttr() { return xmlAttribute().withName('service', 'service-name') }

    static XmlAttributePattern groovyEngineAttrValue() { return makeAttrAndValPattern('engine', 'groovy') }

    static XmlAttributePattern groupEngineAttrValue() { return makeAttrAndValPattern('engine', 'group') }

    static XmlAttributePattern javaEngineAttrValue() { return makeAttrAndValPattern('engine', 'java') }

    static XmlAttributePattern typeScreenAttrValue() { return makeAttrAndValPattern('type', 'screen') }

    static XmlAttributePattern javaTypeAttrValue() { return makeAttrAndValPattern('type', 'java') }

    static XmlAttributePattern serviceTypeAttrValue() { return makeAttrAndValPattern('type', 'service') }

    static Capture decoratorScreenTag() { return makeTagPattern('decorator-screen') }

    static Capture viewMapTag() { return makeTagPattern('view-map') }

    static Capture includeScreenTag() { return makeTagPattern('include-screen') }

    static Capture includeMenuTag() { return makeTagPattern('include-menu') }

    static Capture eventTag() { return makeTagPattern('event') }

    static Capture screenletTag() { return makeTagPattern('screenlet') }

    static Capture gridTag() { return makeTagPattern('grid') }

    static Capture includeGridTag() { return makeTagPattern('include-grid') }

    static Capture includeFormTag() { return makeTagPattern('include-form') }

    static Capture formTag() { return makeTagPattern('form') }

    static Capture responseTag() { return makeTagPattern('response') }

    static Capture serviceTag() { return makeTagPattern('service') }

    static Capture groupTag() { return makeTagPattern('group') }

    static Capture invokeTag() { return makeTagPattern('invoke') }

    static Capture aliasTag() { xmlTag().withName("alias") }

    static Capture eventTagInSiteConfNs() {
        return xmlTag().withName("${SITE_CONF_NS_PREFIX}event").withNamespace(SITE_CONF_NS_URL)
    }

    static Capture decoratorScreenTagInScreenNs() {
        return xmlTag().withName("${SCREEN_NS_PREFIX}decorator-screen").withNamespace(SCREEN_NS_URL)
    }

    static Capture includeScreenInFormNs() {
        return xmlTag().withName("${FORM_NS_PREFIX}include-screen").withNamespace(FORM_NS_URL)
    }

    static Capture gridTagInScreenNs() {
        return xmlTag().withName("${SCREEN_NS_PREFIX}include-grid").withNamespace(SCREEN_NS_URL)
    }

    static Capture gridTagInFormNs() {
        return xmlTag().withName("${FORM_NS_PREFIX}include-grid").withNamespace(FORM_NS_URL)
    }

    static Capture includeScreenInScreenNs() {
        return xmlTag().withName("${SCREEN_NS_PREFIX}include-screen").withNamespace(SCREEN_NS_URL)
    }

    static Capture includeMenuInFormNs() {
        return xmlTag().withName("${FORM_NS_PREFIX}include-menu").withNamespace(FORM_NS_URL)
    }

    static Capture includeMenuInScreenNs() {
        return xmlTag().withName("${SCREEN_NS_PREFIX}include-menu").withNamespace(SCREEN_NS_URL)
    }

    static Capture viewMapInSiteConfNs() {
        return xmlTag().withName("${SITE_CONF_NS_PREFIX}view-map").withNamespace(SITE_CONF_NS_URL)
    }

    static Capture formTagInScreenNs() {
        return xmlTag().withName("${SCREEN_NS_PREFIX}include-form").withNamespace(SCREEN_NS_URL)
    }

    static Capture includeFormTagInFormNs() {
        return xmlTag().withName("${FORM_NS_PREFIX}include-form").withNamespace(FORM_NS_URL)
    }

    static Capture formTagWithFormNs() {
        return xmlTag().withName("${FORM_NS_PREFIX}form").withNamespace(FORM_NS_URL)
    }

    static Capture responseTagInSiteConfNs() {
        return xmlTag().withName("${SITE_CONF_NS_PREFIX}response").withNamespace(SITE_CONF_NS_URL)
    }
}
