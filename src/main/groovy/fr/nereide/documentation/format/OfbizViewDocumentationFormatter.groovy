package fr.nereide.documentation.format


import com.intellij.openapi.util.text.HtmlChunk.Element
import fr.nereide.dom.EntityModelFile.ViewEntity

class OfbizViewDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatViewDefinition(ViewEntity view) {
        return formatEntityOrViewDefinition(view)
    }
}
