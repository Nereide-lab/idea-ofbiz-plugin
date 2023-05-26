package org.apache.ofbiz;

import org.apache.ofbiz.entity.model.DynamicViewEntity;

public class EntityFieldCompletionInSimpleAddAliasInDynamicView {
    void myFunction() {
        DynamicViewEntity myDve = new DynamicViewEntity();
        myDve.addMemberEntity("OI", "DunderMifflin");
        myDve.addAlias("OI", "<caret>");
    }
}