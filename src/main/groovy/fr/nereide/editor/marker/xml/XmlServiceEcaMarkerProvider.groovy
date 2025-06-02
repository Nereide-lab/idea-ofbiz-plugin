package fr.nereide.editor.marker.xml

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaToken
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlToken
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizJavaPatterns
import fr.nereide.project.pattern.OfbizXmlPatterns
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import java.util.function.Supplier

class XmlServiceEcaMarkerProvider implements LineMarkerProvider {

    @Override
    LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!PluginActivator.getInstance(element.project).isActive()) return null
        if (!OfbizXmlPatterns.SERVICE_DEF_CALL.accepts(element)) return null
        PsiElement elementToRegister = element.getFirstChild()
        if (!(elementToRegister instanceof XmlToken)) return null

        List<PsiElement> ecas = OfbizProjectHelper.getInstance(element.project)
                .getEcasForService(element)
                .collect { it.getXmlElement()?.getNavigationElement() }

        if (ecas && ecas.size() > 0) {
            return new LineMarkerInfo<PsiElement>(
                    elementToRegister,
                    element.getTextRange(),
                    PluginIcons.ECA_ICON,
                    (psiElement) -> 'Eca(s) present on service',
                    new DefaultGutterIconNavigationHandler<>(ecas, 'Ecas detected'),
                    GutterIconRenderer.Alignment.RIGHT,
                    new Supplier<String>() {
                        @Override
                        String get() {
                            return 'Service ECA detected'
                        }
                    }
            )
        }
        return null
    }
}
