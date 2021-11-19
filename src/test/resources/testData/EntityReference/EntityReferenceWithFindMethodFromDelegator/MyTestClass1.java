package org.apache.ofbiz;

import org.apache.ofbiz.entity.Delegator;

public class MyTestClass1 {
    public void myTestFunction(){
        var testData = Delegator.find("MyTest<caret>Entity");
    }
}