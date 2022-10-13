package fr.nereide.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.Namespace
import com.intellij.util.xml.SubTag

interface CompoundFile extends DomElement {

    @Namespace(CompoundFileDescription.FORM_NS)
    interface FormBlocInCpd extends FormFile {}

    @Namespace(CompoundFileDescription.SITE_CONF_NS)
    interface ControlerBlocInCpd extends ControllerFile {}

    @Namespace(CompoundFileDescription.MENU_NS)
    interface MenuBlocInCpd extends MenuFile {}

    @Namespace(CompoundFileDescription.SCREEN_NS)
    interface ScreenBlocInCpd extends ScreenFile {}

    @SubTag('forms')
    FormBlocInCpd getForms()

    @SubTag('site-conf')
    ControlerBlocInCpd getSiteConf()

    @SubTag('menus')
    MenuBlocInCpd getMenus()

    @SubTag('screens')
    ScreenBlocInCpd getScreens()
}
