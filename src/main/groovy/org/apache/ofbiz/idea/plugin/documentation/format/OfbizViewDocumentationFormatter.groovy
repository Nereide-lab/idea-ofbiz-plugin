package org.apache.ofbiz.idea.plugin.documentation.format


import com.intellij.openapi.util.text.HtmlChunk.Element
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.ViewEntity

class OfbizViewDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatViewDefinition(ViewEntity view) {
        return formatEntityOrViewDefinition(view)
    }
}
