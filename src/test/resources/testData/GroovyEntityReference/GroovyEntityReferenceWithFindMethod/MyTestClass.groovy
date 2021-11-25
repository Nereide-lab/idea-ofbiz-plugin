package org.apache.ofbiz

import org.apache.ofbiz.entity.Delegator;

class MyTestClass {
    void myTestFunction() {
        var testData = Delegator.find("Lob<caret>ster")
    }
}