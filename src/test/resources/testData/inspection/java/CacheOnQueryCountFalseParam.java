import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery

class MyClassroom {
    long defEnt(Delegator delegator) {
        return EntityQuery.use(delegator)
                .from("Gustave")
                .cac<caret>he(false)
                .queryCount();
    }
}
