package fr.nereide.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.Namespace
import com.intellij.util.xml.SubTag

interface CompoundFile extends DomElement {


    @Namespace(CompoundFileDescription.FORM_NS)
    interface FormsInCpd extends FormFile {}

    @Namespace(CompoundFileDescription.SITE_CONF_NS)
    interface ControlersInCpd extends ControllerFile {}

    @Namespace(CompoundFileDescription.MENU_NS)
    interface MenusInCpd extends MenuFile {}

    @Namespace(CompoundFileDescription.SCREEN_NS)
    interface ScreensInCpd extends ScreenFile {}

    @SubTag('menus')
    MenusInCpd getMenus()

    @SubTag('screens')
    ScreensInCpd getScreens()

    @SubTag('forms')
    FormsInCpd getForms()

    @SubTag('site-conf')
    ControlersInCpd getSiteConf()
}
