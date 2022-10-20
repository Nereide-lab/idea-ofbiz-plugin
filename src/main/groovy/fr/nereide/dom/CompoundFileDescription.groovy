package fr.nereide.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class CompoundFileDescription<S extends DomElement> extends DomFileDescription<CompoundFile> {
    private static final String rootTagName = "compound-widgets"

    public static final String SITE_CONF_NS_URL = 'http://ofbiz.apache.org/Site-Conf'
    public static final String SITE_CONF_NS = 'sc'

    public static final String FORM_NS_URL = 'http://ofbiz.apache.org/Widget-Form'
    public static final String FORM_NS = 'wf'
    public static final String FORM_NS_PREFIX = 'wf:'

    public static final String MENU_NS_URL = 'http://ofbiz.apache.org/Widget-Menu'
    public static final String MENU_NS = 'wm'

    public static final String SCREEN_NS_URL = 'http://ofbiz.apache.org/Widget-Screen'
    public static final String SCREEN_NS = 'ws'
    public static final String SCREEN_NS_PREFIX = 'ws:'

    CompoundFileDescription() { super(CompoundFile.class, rootTagName) }

    @Override
    protected void initializeFileDescription() {
        registerNamespacePolicy(SITE_CONF_NS, SITE_CONF_NS_URL)
        registerNamespacePolicy(FORM_NS, FORM_NS_URL)
        registerNamespacePolicy(MENU_NS, MENU_NS_URL)
        registerNamespacePolicy(SCREEN_NS, SCREEN_NS_URL)
    }
}
