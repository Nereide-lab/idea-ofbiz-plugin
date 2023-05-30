package fr.nereide.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

class EmptyFileLocationInspection extends LocalInspectionTool {

    private final MoveFileToCorrectLocationFix myQuickFix = new MoveFileToCorrectLocationFix()

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

    private static class MoveFileToCorrectLocationFix implements LocalQuickFix {

        @Override
        String getName() {
            return InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.fixstring')
        }

        @Override
        String getFamilyName() {
            return getName()
        }

        @Override
        void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {

        }
    }
}
