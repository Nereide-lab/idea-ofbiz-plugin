package org.apache.ofbiz;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.GenericEntityException;

class EntityFieldCompletionInValueQueriedInTryCatch {
    public void myTestFunction() {
        GenericValue testData;
        try {
            testData = EntityQuery.use(Delegator)
                    .from("DunderMifflin")
                    .queryFirst();
        } catch (GenericEntityException ignored) {
        }
        String foo = testData.get("<caret>");
    }
}