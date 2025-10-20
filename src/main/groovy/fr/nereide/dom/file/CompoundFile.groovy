package fr.nereide.dom.file

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.Namespace
import com.intellij.util.xml.SubTag
import fr.nereide.dom.filedesc.CompoundFileDescription

/**
 * Part of the OFBiz DOM description
 */
interface CompoundFile extends DomElement {

    @Namespace(CompoundFileDescription.FORM_NS)
    interface FormBlocInCpd extends FormFile {}

    @Namespace(CompoundFileDescription.SITE_CONF_NS)
    interface ControllerBlocInCpd extends ControllerFile {}

    @Namespace(CompoundFileDescription.MENU_NS)
    interface MenuBlocInCpd extends MenuFile {}

    @Namespace(CompoundFileDescription.SCREEN_NS)
    interface ScreenBlocInCpd extends ScreenFile {}

    @SubTag('forms')
    FormBlocInCpd getForms()

    @SubTag('site-conf')
    ControllerBlocInCpd getSiteConf()

    @SubTag('menus')
    MenuBlocInCpd getMenus()

    @SubTag('screens')
    ScreenBlocInCpd getScreens()

}
