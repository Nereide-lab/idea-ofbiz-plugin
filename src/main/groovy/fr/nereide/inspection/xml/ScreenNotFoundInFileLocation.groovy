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
import fr.nereide.inspection.quickfix.xml.CreateScreenInScreenFileQuickFix
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

import static fr.nereide.inspection.common.InspectionUtil.fileHasElementWithSameName

class ScreenNotFoundInFileLocation extends LocalInspectionTool {

    final String ROOT = 'screens'
    final String NAMESPACE = CompoundFileDescription.SCREEN_NS_URL

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
                if (!OfbizXmlPatterns.SCREEN_CALL.accepts(attribute.getValueElement())) return

                ProjectServiceInterface ps = attribute.getProject().getService(ProjectServiceInterface.class)
                XmlAttribute locationAttribute = attribute.getParent().getAttribute('location')
                String targetFileLocation
                boolean isController
                (targetFileLocation, isController) = getControllerSafeLocationAttr(locationAttribute ?: attribute)
                PsiFile targetFile = ps.getPsiFileAtLocation(targetFileLocation)

                if (!targetFile) targetFile = attribute.getContainingFile()
                if (!targetFile || !(targetFile instanceof XmlFile)) return

                String nameValue = isController ? getControllerSafeScreenName(attribute) : attribute.value
                if (!fileHasElementWithSameName(targetFile, ROOT, NAMESPACE, nameValue)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            new CreateScreenInScreenFileQuickFix(targetFile, isController ?
                                    getScreenFromController(attribute.value) : attribute.value)
                    )
                }
            }
        }
    }

    static String getScreenFromController(String value) {
        return value.substring(value.lastIndexOf('#') + 1, value.size())
    }

    static def getControllerSafeLocationAttr(XmlAttribute location) {
        if (!location.value) return null
        String val = location.value
        if (val.contains('$')) {
            return [null, false]
        } else if (val.contains('#')) {
            return [val.substring(0, val.lastIndexOf('#')), true]
        }
        return [val, false]
    }

    static String getControllerSafeScreenName(XmlAttribute location) {
        if (!location.value) return null
        String val = location.value
        if (val.contains('#')) {
            return val.substring(val.lastIndexOf('#') + 1, val.length())
        }
        return val
    }
}
