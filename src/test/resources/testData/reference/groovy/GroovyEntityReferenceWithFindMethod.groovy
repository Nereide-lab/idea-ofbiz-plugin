import org.apache.ofbiz.entity.Delegator

def foo(){
    def bar = Delegator.find("Lob<caret>ster")
}