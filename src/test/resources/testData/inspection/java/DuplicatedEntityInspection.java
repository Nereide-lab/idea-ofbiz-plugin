package org.apache.ofbiz;

import java.util.List;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

public class DuplicatedEntityInspection {
    public void myTestFunction() {
        List<GenericValue> testDataList = EntityQuery.use(Delegator)
                .from("DuplicatedEntityInspection")
                .queryList();
    }
}
