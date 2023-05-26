package org.apache.ofbiz

import org.apache.ofbiz.entity.model.DynamicViewEntity

def myDve = new DynamicViewEntity()
myDve.addMemberEntity("DF", "DunderMifflin")
myDve.addAlias("DF", "<caret>")
