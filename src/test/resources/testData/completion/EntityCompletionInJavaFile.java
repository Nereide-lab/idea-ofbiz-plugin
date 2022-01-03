package org.apache.ofbiz;

import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.Delegator;

public class EntityCompletionInJavaFile {
    public void myTestFunction() {
        var testData = EntityQuery.from("<caret>");
    }
}
