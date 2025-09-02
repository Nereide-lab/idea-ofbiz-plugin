package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.element.controller.RequestMap
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.utils.XmlUtils
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
        Map<String, Set<RequestMap>> componentUrisByMountPoint = OfbizProjectHelper
                .getInstance(attributeValue.project)
                .getStructuredComponentRequestMaps(MiscUtils.getComponentName(attributeValue))
        return componentUrisByMountPoint.find { String mountPoint, Set<RequestMap> reqMaps ->
            return reqMaps.any { reqMap ->
                String comparedUri = reqMap.uri.value // TODO debug to remove
                hasSameUriAndMethodAndMountPoint(mountPoint, reqMap, attributeValue)
            }
        }
    }

    private static boolean hasSameUriAndMethodAndMountPoint(String comparedMP, RequestMap comparedRM, XmlAttributeValue myUriAttr) {
        if (comparedRM.uri.value != myUriAttr.value || comparedRM.getXmlTag() == XmlUtils.getParentTag(myUriAttr)) {
            return false
        }
        XmlTag myRequestMap = XmlUtils.getParentTag(myUriAttr)
        String myMethod = myRequestMap.getAttribute('method')?.value
        Set<String> myMountPoints = OfbizProjectHelper
                .getInstance(myUriAttr.project)
                .getMountPointsOfUri(myUriAttr)
        boolean isSameMountPoint = myMountPoints.contains(comparedMP)

        String comparedMethod = comparedRM.method.value ?: 'get'

        if (!myMethod) {
            if (!comparedMethod && isSameMountPoint) {
                return true
            } else myMethod = 'get'
        }

        boolean isSameMethod = myMethod == comparedMethod
        return isSameMethod && isSameMountPoint
    }
}

