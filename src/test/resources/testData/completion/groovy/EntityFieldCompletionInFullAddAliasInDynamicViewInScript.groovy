import org.apache.ofbiz.entity.model.DynamicViewEntity

void myFunction() {
    DynamicViewEntity myDve = new DynamicViewEntity()
    myDve.addMemberEntity("DM", "DunderMifflin")
    myDve.addAlias("DM", "name", "<caret>", null, null, null, null)
}