package org.apache.ofbiz
import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery
import org.apache.ofbiz.service.LocalDispatcher
class EntityFieldCompletionInValueQueriedInTryCatch {
       def myFunction(LocalDispatcher dispatcher) {
        Delegator delegator = dispatcher.getDelegator()
        GenericValue myVal = null
        try {
            myVal = EntityQuery.use(delegator)
                    .from('RossAndSister')
                    .queryFirst()
        } catch (Exception ignored) {
        }
        myVal.get('<caret>')
    }

}
