package org.apache.ofbiz;

import org.apache.ofbiz.entity.Delegator;

public class MyTestClass {
    public void myTestFunction() {
        var testData = Delegator.findAll("TwoF<caret>lowers");
    }
}