package fr.nereide.editor.marker

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.patterns.PsiElementPattern
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.ui.awt.RelativePoint
import fr.nereide.project.PluginActivator
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import java.awt.event.MouseEvent
import java.util.function.Supplier

import static com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment

abstract class OfbizBaseLineMarker implements LineMarkerProvider {

    abstract PsiElementPattern getPattern()

    abstract Class getLeafElementType()

    abstract PsiTargetPresentationRenderer<PsiElement> getRenderer()

    abstract String getListTitle()

    abstract Closure<String> getTooltipProvider(List<PsiElement> navEls)

    abstract Supplier<String> getMessageSupplier()

    abstract List<PsiElement> getNavigatableList(PsiElement element)

    @Override
    LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!PluginActivator.getInstance(element.project).isActive()) return null
        if (!getPattern().accepts(element)) return null
        PsiElement elementToRegister = element.getFirstChild()
        if (!getLeafElementType().isAssignableFrom(elementToRegister.getClass())) return null

        List<PsiElement> navEls = getNavigatableList(element)

        if (navEls.size() < 1) return null
        return new LineMarkerInfo<PsiElement>(
                elementToRegister,
                element.getTextRange(),
                PluginIcons.ECA_ICON,
                getTooltipProvider(navEls),
                getNavHandler(navEls),
                Alignment.RIGHT,
                getMessageSupplier()
        )
    }

    private GutterIconNavigationHandler<PsiElement> getNavHandler(List<PsiElement> navigatableList) {
        return new GutterIconNavigationHandler<PsiElement>() {
            @Override
            void navigate(MouseEvent e, PsiElement elt) {
                if (navigatableList.size() == 1) {
                    Navigatable elementAlone = navigatableList.first as Navigatable
                    if (elementAlone.canNavigate()) elementAlone.navigate(true)
                } else {
                    NavigationUtil.getPsiElementPopup(
                            { -> navigatableList },
                            getRenderer(),
                            getListTitle(),
                            elt.project
                    ).show(new RelativePoint(e))
                }
            }
        }
    }
}
