package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.xml.AdjustFileLocationPathFix
import fr.nereide.inspection.quickfix.xml.CreateFileAtLocationFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

/**
 * Inspection that checks files references to a non existing file
 */
class EmptyFileLocationInspection extends OfbizBaseInspection {

    private final AdjustFileLocationPathFix myChangePathQuickFix = new AdjustFileLocationPathFix()
    private final CreateFileAtLocationFix myCreateFileQuickFix = new CreateFileAtLocationFix()

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!attribute.name || !(['path', 'location'].contains(attribute.name))) {
                    return
                }
                String attrValue = attribute.value
                if (!attrValue || (!attrValue.startsWith('component://')) || (attrValue.contains('${'))) {
                    return
                }

                // Actual control
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.originalElement.project)
                if (!ph.getPsiFileAtLocation(attrValue)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myChangePathQuickFix,
                            doesComponentExists(ph, attrValue) ? myCreateFileQuickFix : null
                    )
                }
            }

        }
    }

    private static PsiDirectory doesComponentExists(OfbizProjectHelper ph, String attrValue) {
        return ph.getComponentDir(FileHandlingUtils.splitPathToList(attrValue)[0])
    }

}
