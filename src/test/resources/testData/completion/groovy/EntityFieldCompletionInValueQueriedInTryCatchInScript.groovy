def myMethod() {
    GenericValue lookedValue
    try {
        lookedValue = from('RossAndSister').where('<caret>').queryOne()
    } catch (Exception e) {}
    lookedValue.<caret>
}
