package org.apache.ofbiz;

import org.apache.ofbiz.base.util.UtilProperties;

public class HoverDocOnPropertyInJava {
    void myTestFunction() {
        UtilProperties.getMessage("foo", "Test<caret>Pivooot", null);
    }
}