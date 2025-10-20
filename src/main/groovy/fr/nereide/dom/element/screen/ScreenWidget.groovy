package fr.nereide.dom.element.screen

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

/**
 * Part of the OFBiz DOM description
 */
interface ScreenWidget extends DomElement {

    @SubTagList('include-screen')
    List<IncludeScreen> getIncludeScreens()

}
