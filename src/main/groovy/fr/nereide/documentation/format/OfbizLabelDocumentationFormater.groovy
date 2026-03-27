/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.documentation.format

import static com.intellij.lang.documentation.DocumentationMarkup.CONTENT_ELEMENT
import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.Element
import static com.intellij.openapi.util.text.HtmlChunk.text

import com.intellij.openapi.util.text.HtmlBuilder
import fr.nereide.dom.element.uilabel.Property
import fr.nereide.dom.element.uilabel.PropertyValue
import fr.nereide.project.utils.MiscUtils

/**
 * Formatter for documentation
 */
class OfbizLabelDocumentationFormater extends OfbizCommonDocumentationFormatter {

    static Element formatPropertyDefinition(Property property) {
        String fileName = property.xmlElement.containingFile.name
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(text("Property key=\"${property.key}\"").bold())
                .br()
                .append(text("Defined in ${fileName}, [Component: ${MiscUtils.getComponentName(property)}]"))
        return docBuilder
                .wrapWith('pre')
                .wrapWith(DEFINITION_ELEMENT)
    }

    static Element formatPropertyText(Property property) {
        String labelString
        List<PropertyValue> values = property.propertyValues
        PropertyValue propValue = values.find { propertyVal -> propertyVal.lang.value.startsWith('en') }
        labelString = propValue ? propValue.tagValue : 'NO TEXT WAS FOUND FOR ENGLISH LANG IN FILE FOR THIS KEY'
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(text('English :').bold()).nbsp()
                .br()
                .append(text(labelString))
        return docBuilder.wrapWith(CONTENT_ELEMENT)
    }

}
