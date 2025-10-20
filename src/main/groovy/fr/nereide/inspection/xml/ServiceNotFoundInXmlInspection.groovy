package fr.nereide.inspection.xml

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

/**
 * Basic inspection for service not found
 */
class ServiceNotFoundInXmlInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                if (PluginActivator.getInstance(val.project).inactive ||
                        !OfbizXmlPatterns.SERVICE_DEF_CALL.accepts(val)) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(val.project)
                if (!(ph.getService(val.value))) {
                    holder.registerProblem(
                            val,
                            message('inspection.service.not.found.display.descriptor'),
                            WARNING)
                }
            }

        }
    }

}
