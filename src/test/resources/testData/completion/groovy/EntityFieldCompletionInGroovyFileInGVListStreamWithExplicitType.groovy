package org.apache.ofbiz

List<org.apache.ofbiz.entity.GenericValue> rings = from('Mordor')

rings.stream().filter({ GenericValue it ->
    it.<caret>
})