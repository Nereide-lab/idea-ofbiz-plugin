package fr.nereide.inspection.xml

import com.intellij.patterns.XmlAttributeValuePattern
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import fr.nereide.inspection.common.GenericDuplicateElementXmlInspection
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.xml.ScreenReference

import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedScreenInspection extends GenericDuplicateElementXmlInspection {

    final XmlAttributeValuePattern myDefinitionPattern = OfbizXmlPatterns.SCREEN_NAME_IN_DEFINITION
    final XmlAttributeValuePattern myPattern = OfbizXmlPatterns.SCREEN_CALL
    final String myMessage = message('inspection.screen.duplicate.display.descriptor')

    boolean isDuplicate(XmlAttributeValue val, boolean isDef) {
        XmlElement valToUse = isDef ? val : new ScreenReference(val, true).resolve() as XmlElement
        if (!valToUse) return false
        return val.getProject().getService(ProjectServiceInterface.class)
                .getAllScreenFromCurrentFileFromElement(valToUse)
                .findAll { it.name.value == val.value }
                .size() > 1
    }

    XmlAttributeValuePattern getMyDefinitionPattern() {
        return myDefinitionPattern
    }

    XmlAttributeValuePattern getMyPattern() {
        return myPattern
    }

    String getMyMessage() {
        return myMessage
    }
}
