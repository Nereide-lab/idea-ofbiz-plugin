package fr.nereide.editor.marker.xml

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.patterns.PsiElementPattern
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import javax.swing.*
import java.awt.*
import java.util.List
import java.util.function.Supplier

class XmlExtendedEntityMarkerProvider extends OfbizBaseLineMarker {

    PsiElementPattern getPattern() { return OfbizXmlPatterns.ENTITY_OR_VIEW_CALL }

    Class getLeafElementType() { return XmlToken.class }

    Icon getIcon() {
        return PluginIcons.ENTITY_ICON
    }

    PsiTargetPresentationRenderer<PsiElement> getRenderer() {
        return new ExtentedEntityPresentationRenderer()
    }

    String getListTitle() {
        return 'Extends list'
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> 'Entity is extended'
    }

    Supplier<String> getMessageSupplier() {
        return { 'Entity is extended' }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getExtendEntityListForEntity(element)
                .collect { it.getXmlElement().getNavigationElement() }
    }
}

class ExtentedEntityPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

    String getElementText(PsiElement element) {
        return "Entity extended in ${MiscUtils.getComponentName(element)}]"
    }

    @Override
    TargetPresentation getPresentation(@NotNull PsiElement element) {

        //noinspection UnstableApiUsage
        return new TargetPresentation() {

            String getPresentableText() { return '' }

            String getContainerText() { return '' }

            String getLocationText() { return "[Component : ${MiscUtils.getComponentName(element)}]" }

            Icon getIcon() { return PluginIcons.ECA_ICON }

            TextAttributes getPresentableTextAttributes() { return null }

            TextAttributes getContainerTextAttributes() { return null }

            Icon getLocationIcon() { return null }

            Color getBackgroundColor() { return null }
        }
    }
}