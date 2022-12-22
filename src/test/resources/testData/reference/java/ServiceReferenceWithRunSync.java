package org.apache.ofbiz;

import org.apache.ofbiz.service.LocalDispatcher;

public class MyTestClass {
    public void myTestFunction() {
        var testData = LocalDispatcher.runSync("pivo<caret>t", null);
    }
}
