package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedUriInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                if (!PluginActivator.getInstance(val.project).isActive()) return
                boolean isInController = OfbizXmlPatterns.URI_IN_REQUEST_DEFINITION.accepts(val)
                boolean isElsewhereRelevant = OfbizXmlPatterns.URI_CALL.accepts(val)
                if (!(isInController || isElsewhereRelevant)) return

                if (isDuplicated(val)) {
                    holder.registerProblem(
                            val,
                            message('inspection.uri.duplicate.display.descriptor'),
                            WARNING)
                }
            }

        }
    }

    private static boolean isDuplicated(XmlAttributeValue attributeValue) {
        return OfbizProjectHelper.getInstance(attributeValue.project)
                .getComponentRequestMaps(MiscUtils.getComponentName(attributeValue))
                .findAll { it.uri.value == attributeValue.value }
                .size() > 1
    }

}

