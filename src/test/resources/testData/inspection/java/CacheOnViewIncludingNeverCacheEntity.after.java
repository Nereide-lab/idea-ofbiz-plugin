import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery

class MyClassroom {
    GenericValue defEnt(Delegator delegator) {
        return EntityQuery.use(delegator)
                .from("MyNeverCachedView")
                .queryFirst();
    }
}
