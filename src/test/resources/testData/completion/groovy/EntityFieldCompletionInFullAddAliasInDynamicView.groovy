package org.apache.ofbiz

import org.apache.ofbiz.entity.model.DynamicViewEntity

class EntityFieldCompletionInFullAddAliasInDynamicView {
    void myFunction() {
        DynamicViewEntity myDve = new DynamicViewEntity()
        myDve.addMemberEntity("DM", "DunderMifflin")
        myDve.addAlias("DM", "name", "<caret>", null, null, null, null)
    }
}

