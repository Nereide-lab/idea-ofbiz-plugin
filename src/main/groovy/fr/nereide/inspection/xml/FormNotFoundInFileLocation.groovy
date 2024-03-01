package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import fr.nereide.dom.CompoundFileDescription
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.quickfix.xml.CreateFormInFormFileQuickFix
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

import static fr.nereide.inspection.common.InspectionUtil.fileHasElementWithSameName

class FormNotFoundInFileLocation extends LocalInspectionTool {

    final String ROOT = 'forms'
    final String NAMESPACE = CompoundFileDescription.FORM_NS_URL

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
                if (!OfbizXmlPatterns.FORM_CALL.accepts(attribute.getValueElement())) return
                ProjectServiceInterface ps = attribute.getProject().getService(ProjectServiceInterface.class)
                XmlAttribute locationAttribute = attribute.getParent().getAttribute('location')
                XmlAttribute extendsLocationAttribute = attribute.getParent().getAttribute('extends-resource')
                PsiFile targetFile
                if (locationAttribute) {
                    targetFile = ps.getPsiFileAtLocation(locationAttribute.value)
                } else if (extendsLocationAttribute) {
                    if (extendsLocationAttribute.value.contains('$')) return // dynamic case, just ignore it
                    targetFile = ps.getPsiFileAtLocation(extendsLocationAttribute.value)
                } else {
                    targetFile = attribute.getContainingFile()
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
