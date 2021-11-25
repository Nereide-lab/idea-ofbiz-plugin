package org.apache.ofbiz;

import org.apache.ofbiz.entity.model.DynamicViewEntity;

public class MyTestClass {
    public void myTestFunction() {
        var testData = DynamicViewEntity.addMemberEntity(null, "WeWe<caret>reOnABreak");
    }
}