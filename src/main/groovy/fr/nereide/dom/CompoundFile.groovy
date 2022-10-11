package fr.nereide.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.Namespace
import com.intellij.util.xml.SubTagList

import static fr.nereide.dom.FormFile.*
import static fr.nereide.dom.MenuFile.*
import static fr.nereide.dom.ScreenFile.*

interface CompoundFile extends DomElement {


    @Namespace(CompoundFileDescription.FORM_NS)
    interface FormInCpd extends Form {}

    @Namespace(CompoundFileDescription.SITE_CONF_NS)
    interface ControlerInCpd extends ControllerFile {}

    @Namespace(CompoundFileDescription.MENU_NS)
    interface MenuInCpd extends Menu {}

    @Namespace(CompoundFileDescription.SCREEN_NS)
    interface ScreenInCpd extends Screen {}

    @SubTagList('menus')
    List<MenuInCpd> getMenus()

    @SubTagList('screens')
    List<ScreenInCpd> getScreens()

    @SubTagList('forms')
    List<FormInCpd> getForms()

    @SubTagList('site-conf')
    ControlerInCpd getSiteConf()
}
