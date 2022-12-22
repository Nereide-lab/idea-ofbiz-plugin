package org.apache.ofbiz;

import org.apache.ofbiz.service.LocalDispatcher;

public class MyTestClass {
    public void myTestFunction() {
        var testData = LocalDispatcher.schedule("p<caret>ivot", null, null, null, null, null, null);
    }
}