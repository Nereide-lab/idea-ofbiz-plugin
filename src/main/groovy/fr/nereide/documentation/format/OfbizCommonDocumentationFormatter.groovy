package fr.nereide.documentation.format

import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.util.xml.DomElement
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.project.utils.MiscUtils

import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.*

class OfbizCommonDocumentationFormatter {

    static String formatNavigateDocWithDomElement(DomElement element, String elementType, String elementName) {
        HtmlBuilder docBuilder = new HtmlBuilder()
        String containingFile = element.getXmlElement().getContainingFile().getName()
        docBuilder.append(text("Ofbiz ${elementType}: ${elementName}"))
                .br()
                .append(text("Defined in ${containingFile} [component: ${MiscUtils.getComponentName(element)}]"))
        return docBuilder.toString()
    }


    static Element formatEntityOrViewDefinition(DomElement element) {
        boolean isView = element instanceof ViewEntity
        boolean isEntity = element instanceof Entity
        if (!isView && !isEntity) return null
        HtmlBuilder builder = new HtmlBuilder()

        String fileName = element.getXmlElement().getContainingFile().getName()
        builder.append(text("<${isView ? "view" : "entity"} entity-name=\"${element.getEntityName()}\"").bold())
                .append(nbsp())
                .append(text("package-name=\"${element.getPackageName()}\""))
                .append(nbsp())
                .append(text("title=\"${element.getTitle()}\" />"))
                .br()
                .append(text("Description: ${element.getDescription()}"))
                .br()
                .append(text("Defined in ${fileName}, [Component: ${MiscUtils.getComponentName(element)}]"))
        return builder.wrapWith("pre").wrapWith(DEFINITION_ELEMENT)
    }
}
