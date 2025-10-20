package fr.nereide.inspection.xml

import static fr.nereide.inspection.common.InspectionUtil.fileHasElementWithSameName
import static fr.nereide.project.pattern.OfbizPluginConstants.FILE_AND_ELEMENT_SEPARATOR

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
import fr.nereide.inspection.quickfix.xml.CreateScreenInScreenFileQuickFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

/**
 * Custom inspection that checks if referenced screen exists
 */
class ScreenNotFoundInFileLocationInspection extends OfbizBaseInspection {

    public static final String ROOT = 'screens'
    public static final String NAMESPACE = CompoundFileDescription.SCREEN_NS_URL

    static String getScreenFromController(String value) {
        return value.substring(value.lastIndexOf(FILE_AND_ELEMENT_SEPARATOR) + 1, value.size())
    }

    static List getControllerSafeLocationAttr(XmlAttribute location) {
        if (!location.value) return Collections.emptyList()
        String val = location.value
        if (val.contains('$')) {
            return [null, false]
        } else if (val.contains(FILE_AND_ELEMENT_SEPARATOR)) {
            return [val.substring(0, val.lastIndexOf(FILE_AND_ELEMENT_SEPARATOR)), true]
        }
        return [val, false]
    }

    static String getControllerSafeScreenName(XmlAttribute location) {
        if (!location.value) return ''
        String val = location.value
        if (val.contains(FILE_AND_ELEMENT_SEPARATOR)) {
            return val.substring(val.lastIndexOf(FILE_AND_ELEMENT_SEPARATOR) + 1, val.length())
        }
        return val
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!OfbizXmlPatterns.SCREEN_CALL.accepts(attribute.valueElement)) return

                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.project)
                XmlAttribute locationAttribute = attribute.parent.getAttribute('location')
                String targetFileLocation
                boolean isController
                (targetFileLocation, isController) = getControllerSafeLocationAttr(locationAttribute ?: attribute)
                PsiFile targetFile = ph.getPsiFileAtLocation(targetFileLocation) ?: attribute.containingFile
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

}
