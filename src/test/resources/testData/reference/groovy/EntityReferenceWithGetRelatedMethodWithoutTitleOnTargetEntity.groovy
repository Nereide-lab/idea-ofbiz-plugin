import org.apache.ofbiz.entity.GenericValue

Map foo() {
    GenericValue foo = from('Hakuna').where('foo', 'bar').queryList()
    def bar = foo.getRelated('Mata<caret>ta', null, null, false)
}