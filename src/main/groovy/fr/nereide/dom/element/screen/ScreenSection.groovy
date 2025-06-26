package fr.nereide.dom.element.screen

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

interface ScreenSection extends DomElement {

    @SubTagList('widgets')
    List<ScreenWidget> getWidgets()
}
