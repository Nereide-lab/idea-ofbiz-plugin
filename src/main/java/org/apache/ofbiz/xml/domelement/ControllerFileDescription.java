package org.apache.ofbiz.xml.domelement;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileDescription;


public class ControllerFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "site-conf";

    public ControllerFileDescription() { super(ControllerFile.class, rootTagName); }
}

