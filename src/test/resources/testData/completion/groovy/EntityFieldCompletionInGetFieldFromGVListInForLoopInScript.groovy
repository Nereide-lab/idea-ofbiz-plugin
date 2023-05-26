import org.apache.ofbiz.entity.GenericValue

List<GenericValue> myVals = from('RossAndSister').where('foo', 'bar').queryList()
for (GenericValue myVal : myVals) {
    myVal.get('<caret>')
}
