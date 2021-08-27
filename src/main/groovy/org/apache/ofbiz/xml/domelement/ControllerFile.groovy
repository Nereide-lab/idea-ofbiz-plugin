package org.apache.ofbiz.xml.domelement

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

public interface ControllerFile extends DomElement {

    /* Request */

    @SubTagList("request-map")
    List<RequestMap> getRequestMap();

    interface RequestMap extends ControllerFile {
        @NameValue
        @Attribute("uri")
        GenericAttributeValue<String> getUri();
    }

    /* View Map */

    @SubTagList("view-map")
    List<ViewMap> getViewMap();

    public interface ViewMap extends ControllerFile {
        @NameValue
        @Attribute("name")
        GenericAttributeValue<String> getName();

        @Attribute("type")
        GenericAttributeValue<String> getType();

    }

}

