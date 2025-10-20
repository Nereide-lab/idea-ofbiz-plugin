package fr.nereide.inspection.xml

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.common.ServiceReference
import org.jetbrains.annotations.NotNull

/**
 * Basic inspection for service duplicates
 */
class DuplicatedServiceXmlInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                if (PluginActivator.getInstance(val.project).inactive) return
                if (!OfbizXmlPatterns.SERVICE_DEF_CALL.accepts(val)) return
                if (!(new ServiceReference(val).multiResolve(false)?.size() > 1)) return
                holder.registerProblem(
                        val,
                        message('inspection.service.duplicate.display.descriptor'),
                        WARNING)
            }

        }
    }

}
