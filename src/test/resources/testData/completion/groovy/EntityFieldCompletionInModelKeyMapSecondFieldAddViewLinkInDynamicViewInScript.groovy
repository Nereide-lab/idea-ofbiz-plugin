package org.apache.ofbiz

import org.apache.ofbiz.entity.model.DynamicViewEntity
import org.apache.ofbiz.entity.model.ModelKeyMap

void myFunction() {
    DynamicViewEntity myDve = new DynamicViewEntity()
    myDve.addMemberEntity("SB", "Sabre")
    myDve.addMemberEntity("DM", "DunderMifflin")
    myDve.addViewLink("SB", "DM", Boolean.FALSE, ModelKeyMap.makeKeyMapList("foo", "<caret>"))
}
