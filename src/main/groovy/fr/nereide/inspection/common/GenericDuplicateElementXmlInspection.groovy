package fr.nereide.inspection.common

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.patterns.XmlAttributeValuePattern
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import org.jetbrains.annotations.NotNull

class GenericDuplicateElementXmlInspection extends LocalInspectionTool {

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
                boolean isDefinition = myDefinitionPattern.accepts(val)
                if (!isDefinition && myPattern.accepts(val)) return
                if (!isDuplicate(val, isDefinition)) return
                holder.registerProblem(val, myMessage, ProblemHighlightType.WARNING)
            }
        }
    }

    boolean isDuplicate(XmlAttributeValue myVal, boolean isDef) {
        return false
    }

    XmlAttributeValuePattern getMyDefinitionPattern() {
        return null
    }

    XmlAttributeValuePattern getMyPattern() {
        return null
    }

    String getMyMessage() {
        return null
    }
}
