package fr.nereide.inspection.quickfix.xml

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.NotNull

abstract class BaseOfbizElementCreateQuickFix implements LocalQuickFix {
    XmlFile myFile
    String myElementName

    BaseOfbizElementCreateQuickFix(XmlFile file, String formName) {
        myFile = file
        myElementName = formName
    }

    @Override
    String getFamilyName() {
        return getName()
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        XmlTag rootTag = myFile.getRootTag()
        XmlTag newTag = rootTag.createChildTag(getTagType(), '', '', false)
        newTag.setAttribute('name', myElementName)
        rootTag.addSubTag(newTag, false)
        myFile.navigate(true)
    }

    String getTagType() { return null }
}
