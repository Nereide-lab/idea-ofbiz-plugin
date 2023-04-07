package org.apache.ofbiz;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

public class EntityFieldCompletionInSimpleWhereInEntityQuery {
    public void myTestFunction(Delegator delegator) throws GenericEntityException {
        GenericValue foo = EntityQuery.use(delegator)
                .from("DunderMifflin")
                .where("<caret>", "bar")
                .queryFirst();
    }
}
