import org.apache.ofbiz.entity.GenericValue

Map foo() {
    GenericValue foo = from('Hakuna').where('foo', 'bar').queryList()
    def bar = foo.getRelated('Hak<caret>uMatata1', null, null, false)
}
