package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.quickfix.xml.AdjustFileLocationPathFix
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

class EmptyFileLocationInspection extends LocalInspectionTool {

    private final AdjustFileLocationPathFix myQuickFix = new AdjustFileLocationPathFix()

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
                ProjectServiceInterface ps = attribute.getOriginalElement().getProject().getService(ProjectServiceInterface.class)
                if (!FileHandlingUtils.getTargetFile(attrValue, ps)) {
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
