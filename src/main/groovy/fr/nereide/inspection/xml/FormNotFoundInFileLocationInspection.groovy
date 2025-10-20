package fr.nereide.inspection.xml

import static fr.nereide.inspection.common.InspectionUtil.fileHasElementWithSameName

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import fr.nereide.dom.filedesc.CompoundFileDescription
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.xml.CreateFormInFormFileQuickFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

/**
 * Basic inspection for forms not found
 */
class FormNotFoundInFileLocationInspection extends OfbizBaseInspection {

    public static final String ROOT = 'forms'
    public static final String NAMESPACE = CompoundFileDescription.FORM_NS_URL

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!OfbizXmlPatterns.FORM_CALL.accepts(attribute.valueElement)) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.project)
                XmlAttribute locationAttribute = attribute.parent.getAttribute('location')
                XmlAttribute extendsLocationAttribute = attribute.parent.getAttribute('extends-resource')
                PsiFile targetFile
                if (locationAttribute) {
                    targetFile = ph.getPsiFileAtLocation(locationAttribute.value)
                } else if (extendsLocationAttribute) {
                    if (extendsLocationAttribute.value.contains('$')) return // dynamic case, just ignore it
                    targetFile = ph.getPsiFileAtLocation(extendsLocationAttribute.value)
                } else {
                    targetFile = attribute.containingFile
                }
                if (!targetFile || !(targetFile instanceof XmlFile)) return

                if (!fileHasElementWithSameName(targetFile, ROOT, NAMESPACE, attribute)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            new CreateFormInFormFileQuickFix(targetFile, attribute.value)
                    )
                }
            }

        }
    }

}
