package fr.nereide.editor.marker

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import java.util.function.Supplier

class BaseServiceEcaMarker implements LineMarkerProvider {

    PsiElementPattern getPattern() {
        return null
    }

    Class getLeafElementType() {
        return null
    }

    @Override
    LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!PluginActivator.getInstance(element.project).isActive()) return null
        if (!getPattern().accepts(element)) return null
        PsiElement elementToRegister = element.getFirstChild()
        if (!getLeafElementType().isAssignableFrom(elementToRegister.getClass())) return

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
