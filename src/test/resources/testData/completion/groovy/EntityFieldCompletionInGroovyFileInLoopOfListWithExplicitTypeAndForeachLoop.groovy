package org.apache.ofbiz

List<org.apache.ofbiz.entity.GenericValue> rings = from('Mordor')
rings.forEach { org.apache.ofbiz.entity.GenericValue it ->
    it.<caret>
}