package fr.nereide.inspection.xml

import static com.intellij.codeInspection.ProblemHighlightType.WEAK_WARNING
import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.PluginActivator
import org.jetbrains.annotations.NotNull

/**
 * Low inspection that signal a controller entry that is accessible without security.
 * Thanks gil for the idea.
 */
class NoAuthOnRequestInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlTag(@NotNull XmlTag tag) {
                if (PluginActivator.getInstance(tag.project).inactive) return
                String tagName = tag.name
                if (tagName != 'request-map') return
                XmlTag securityTag = tag.subTags?.find { subtag -> subtag.name == 'security' }
                if (!securityTag) return
                XmlAttribute authAttr = securityTag.getAttribute('auth')
                if (!authAttr || authAttr.value == 'true') return

                holder.registerProblem(
                        tag,
                        message('inspection.controller.no.auth.descriptor'),
                        WEAK_WARNING)
            }

        }
    }

}
