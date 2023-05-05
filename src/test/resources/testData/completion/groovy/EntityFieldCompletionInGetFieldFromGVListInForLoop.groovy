package org.apache.ofbiz

import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery
import org.apache.ofbiz.service.LocalDispatcher

class EntityFieldCompletionInGetFieldFromGVListInForLoop {

    def myFunction(LocalDispatcher dispatcher) {
        Delegator delegator = dispatcher.getDelegator()
        List<GenericValue> myVals = EntityQuery.use(delegator).from('RossAndSister').where('foo', 'bar').queryList()
        for (GenericValue myVal : myVals) {
            myVal.get('<caret>')
        }
    }

}