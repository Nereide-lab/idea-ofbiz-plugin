
package org.apache.ofbiz;

import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.GenericValue;

public class MyTestClass {
    public void myTestFunction() {
        GenericValue testData = EntityQuery.from("Hakuna3").queryFirst();
        testData.getRelated("HakuMatat<caret>a3");
    }
}