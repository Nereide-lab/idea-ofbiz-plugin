package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface AliasAll extends DomElement {

    @Attribute('entity-alias')
    GenericAttributeValue<String> getEntityAlias()

    @Attribute('prefix')
    GenericAttributeValue<String> getPrefix()

    @SubTagList('exclude')
    List<AliasAllExclude> getAliasAllExcludes()

}
