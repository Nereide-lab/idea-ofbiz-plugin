package org.apache.ofbiz

import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery
import org.apache.ofbiz.service.LocalDispatcher

class EntityFieldCompletionInSimpleGetFieldFromGenericValue {
    def myFunction(LocalDispatcher dispatcher) {
        Delegator delegator = dispatcher.getDelegator()
        GenericValue myVal = EntityQuery.use(delegator).from('RossAndSister').where('foo', 'bar').queryFirst()
        myVal.get('<caret>')
    }
}
