package org.apache.ofbiz

import org.apache.ofbiz.entity.model.DynamicViewEntity
import org.apache.ofbiz.entity.model.ModelKeyMap

class EntityFieldCompletionInModelKeyMapFirstFieldAddViewLinkInDynamicView {
    void myFunction() {
        DynamicViewEntity myDve = new DynamicViewEntity()
        myDve.addMemberEntity("DM", "DunderMifflin")
        myDve.addViewLink("DM", "OP", Boolean.FALSE, ModelKeyMap.makeKeyMapList("<caret>"))
    }
}
