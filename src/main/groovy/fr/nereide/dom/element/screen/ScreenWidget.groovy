package fr.nereide.dom.element.screen

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

interface ScreenWidget extends DomElement {

    @SubTagList('include-screen')
    List<IncludeScreen> getIncludeScreens()
}
