package org.apache.ofbiz.idea.plugin.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import org.jetbrains.annotations.NotNull

class EmptyFileLocationInspection extends LocalInspectionTool {

    private final org.apache.ofbiz.idea.plugin.inspection.quickfix.AdjustFileLocationPathFix myQuickFix = new org.apache.ofbiz.idea.plugin.inspection.quickfix.AdjustFileLocationPathFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (attribute.getName() != 'location') {
                    return
                }
                String attrValue = attribute.getValue()
                if (!attrValue.startsWith('component://')) {
                    return
                }

                //Actual control
                org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps = attribute.getOriginalElement().getProject().getService(org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface.class)
                if (!org.apache.ofbiz.idea.plugin.project.utils.FileHandlingUtils.getTargetFile(attrValue, ps)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myQuickFix
                    )
                }
            }
        }
    }
}
