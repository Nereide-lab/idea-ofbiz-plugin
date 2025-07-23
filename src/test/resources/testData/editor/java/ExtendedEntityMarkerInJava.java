package org.apache.ofbiz;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

public class testExtendedEntityMarkerInJava {
    public void myTestFunction() {
        GenericValue testData = EntityQuery.use(Delegator)
                .from("ExtendedEntityMarkerInJavaElement");
    }
}
