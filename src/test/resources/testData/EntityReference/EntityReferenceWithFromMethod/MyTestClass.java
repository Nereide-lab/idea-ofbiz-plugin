package org.apache.ofbiz;

import org.apache.ofbiz.entity.util.EntityQuery;


public class MyTestClass {
    public void myTestFunction() {
        // var testData = EntityQuery.use(/*delegator*/).from("Ri<caret>ck");
        var testData = EntityQuery.from("Ri<caret>ck");
    }
}