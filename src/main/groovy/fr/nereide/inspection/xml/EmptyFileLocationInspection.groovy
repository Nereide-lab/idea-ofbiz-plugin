package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.quickfix.xml.AdjustFileLocationPathFix
import fr.nereide.inspection.quickfix.xml.CreateFileAtLocationFix
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

class EmptyFileLocationInspection extends LocalInspectionTool {

    private final AdjustFileLocationPathFix myChangePathQuickFix = new AdjustFileLocationPathFix()
    private final CreateFileAtLocationFix myCreateFileQuickFix = new CreateFileAtLocationFix()

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
                if (!attribute.getName() || !(['path', 'location'].contains(attribute.getName()))) {
                    return
                }
                String attrValue = attribute.getValue()
                if (!attrValue || (!attrValue.startsWith('component://')) || (attrValue.contains('${'))) {
                    return
                }

                //Actual control
                ProjectServiceInterface ps = attribute.getOriginalElement().getProject().getService(ProjectServiceInterface.class)
                if (!ps.getPsiFileAtLocation(attrValue)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myChangePathQuickFix,
                            doesComponentExists(ps, attrValue) ? myCreateFileQuickFix : null
                    )
                }
            }

        }
    }

    private static PsiDirectory doesComponentExists(ProjectServiceInterface ps, String attrValue) {
        return ps.getComponentDir(FileHandlingUtils.splitPathToList(attrValue)[0])
    }
}
