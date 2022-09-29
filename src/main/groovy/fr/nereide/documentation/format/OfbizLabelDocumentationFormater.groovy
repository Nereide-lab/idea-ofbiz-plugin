package fr.nereide.documentation.format

import com.intellij.openapi.util.text.HtmlBuilder
import fr.nereide.dom.UiLabelFile.Property
import fr.nereide.dom.UiLabelFile.PropertyValue
import fr.nereide.project.utils.MiscUtils

import static com.intellij.lang.documentation.DocumentationMarkup.CONTENT_ELEMENT
import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.Element
import static com.intellij.openapi.util.text.HtmlChunk.text

class OfbizLabelDocumentationFormater extends OfbizCommonDocumentationFormatter {

    static Element formatPropertyDefinition(Property property) {
        String fileName = property.getXmlElement().getContainingFile().getName()
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(text("Property key=\"${property.getKey()}\"").bold())
                .br()
                .append(text("Defined in ${fileName}, [Component: ${MiscUtils.getComponentName(property)}]"))
        return docBuilder
                .wrapWith("pre")
                .wrapWith(DEFINITION_ELEMENT)
    }

    static Element formatPropertyText(Property property) {
        String labelString
        List<PropertyValue> values = property.getPropertyValues()
        Optional<PropertyValue> propValue = values.stream().filter { it.getLang().getValue().startsWith("en") }.findFirst()
        if (!propValue.isPresent()) {
            labelString = "NO TEXT WAS FOUND FOR ENGLISH LANG IN FILE FOR THIS KEY"
        } else {
            labelString = propValue.get().getTagValue()
        }
        HtmlBuilder docBuilder = new HtmlBuilder()
                .append(text("English :").bold()).nbsp()
                .br()
                .append(text(labelString))

        return docBuilder
                .wrapWith(CONTENT_ELEMENT)
    }
}
