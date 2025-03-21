package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.patterns.XmlAttributeValuePattern
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.xml.MenuReference
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static com.intellij.patterns.XmlPatterns.*
import static fr.nereide.dom.filedesc.CompoundFileDescription.MENU_NS_PREFIX
import static fr.nereide.dom.filedesc.CompoundFileDescription.MENU_NS_URL
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedMenuInspection extends LocalInspectionTool {

    @Override
    boolean isEnabledByDefault() {
        return true
    }


    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                boolean isDefinition = MENU_NAME_IN_DEFINITION.accepts(val)
                if (!isDefinition && !OfbizXmlPatterns.MENU_CALL.accepts(val)) return
                if (!isDuplicate(val, isDefinition)) return
                holder.registerProblem(
                        val,
                        message('inspection.menu.duplicate.display.descriptor'),
                        WARNING)
            }
        }
    }

    static boolean isDuplicate(XmlAttributeValue val, boolean isDef) {
        XmlElement valToUse = isDef ? val : new MenuReference(val, true).resolve() as XmlElement
        if (!valToUse) return false
        return val.getProject().getService(ProjectServiceInterface.class)
                .getAllMenuFromCurrentFileFromElement(valToUse)
                .findAll { it.name.value == val.value }
                .size() > 1
    }

    private static final XmlAttributeValuePattern MENU_NAME_IN_DEFINITION = xmlAttributeValue().andOr(
            xmlAttributeValue()
                    .withParent(xmlAttribute('name').withParent(xmlTag().withName('menu'))),
            xmlAttributeValue()
                    .withParent(xmlAttribute('name').withParent(xmlTag().withName("${MENU_NS_PREFIX}menu")
                            .withNamespace(MENU_NS_URL))),
    )
}
