package org.apache.ofbiz;

import org.apache.ofbiz.entity.util.EntityQuery;

public class HoverDocOnServiceInJava {
    void myTestFunction() {
        var delegator = new Delegator()
        def myVar = EntityQuery.use(delegator).from("Vi<caret>").queryFirst()
    }
}
