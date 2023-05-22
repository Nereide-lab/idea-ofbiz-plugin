package org.apache.ofbiz

import org.apache.ofbiz.entity.model.DynamicViewEntity

class EntityFieldCompletionInSimpleAddAliasInDynamicView {
    void myFunction() {
        DynamicViewEntity myDve = new DynamicViewEntity()
        myDve.addMemberEntity("OI", "DunderMifflin")
        myDve.addAlias("OI", "<caret>")
    }
}
