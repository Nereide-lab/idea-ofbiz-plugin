def foo() {
    def bar = from('V<caret>i').where('foo', 'bar').queryOne()
}