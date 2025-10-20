package fr.nereide.dom.element.uilabel

import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.Namespace
import fr.nereide.dom.filedesc.UiLabelFileDescription

/**
 * Custom xml attribute value
 */
@Namespace(UiLabelFileDescription.XML_LANG_NS_NAME)
interface XmlOfbizAttrValue<T> extends GenericAttributeValue<T> {

}
