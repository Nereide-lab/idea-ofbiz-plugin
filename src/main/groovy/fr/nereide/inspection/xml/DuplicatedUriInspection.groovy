package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.patterns.XmlAttributeValuePattern
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static com.intellij.patterns.XmlPatterns.*
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedUriInspection extends LocalInspectionTool {

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue attributeValue) {
                boolean isInController = URI_IN_REQUEST.accepts(attributeValue)
                boolean isElsewhereRelevant = OfbizXmlPatterns.URI_CALL.accepts(attributeValue)
                if (!(isInController || isElsewhereRelevant)) return

                if (isDuplicated(attributeValue)) {
                    holder.registerProblem(
                            attributeValue,
                            message('inspection.uri.duplicate.display.descriptor'),
                            WARNING)
                }
            }

        }
    }

    private static boolean isDuplicated(XmlAttributeValue attributeValue) {
        return attributeValue.getProject().getService(ProjectServiceInterface.class)
                .getComponentRequestMaps(MiscUtils.getComponentName(attributeValue), attributeValue.project)
                .findAll { it.uri.value == attributeValue.value }
                .size() > 1
    }

    private static final XmlAttributeValuePattern URI_IN_REQUEST = xmlAttributeValue()
            .withParent(xmlAttribute('uri').withParent(xmlTag().withName('request-map')))
}

