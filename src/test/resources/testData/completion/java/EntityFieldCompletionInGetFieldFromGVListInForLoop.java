package org.apache.ofbiz;

import java.util.List;

import com.intellij.util.xml.GenericValue;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

public class EntityFieldCompletionInGetFieldFromGVListInForLoop {
    public void myTestFunction() {
        List<GenericValue> testDataList = EntityQuery.use(Delegator)
                .from("DunderMifflin")
                .queryList();
        for (GenericValue testData : testDataList) {
            String foo = testData.get("<caret>");
        }
    }
}
