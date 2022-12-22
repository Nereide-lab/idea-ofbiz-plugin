package org.apache.ofbiz;

import org.apache.ofbiz.service.DispatchContext;

public class MyTestClass {
    public void myTestFunction() {
        var testData = DispatchContext.makeValidContext("pivo<caret>t", null, null);
    }
}
