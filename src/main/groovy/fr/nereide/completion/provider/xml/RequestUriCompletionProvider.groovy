package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomManager
import fr.nereide.dom.CompoundFile
import fr.nereide.dom.FormFile
import fr.nereide.dom.MenuFile
import fr.nereide.dom.ScreenFile
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils
import fr.nereide.project.utils.XmlUtils
import org.jetbrains.annotations.NotNull

import static fr.nereide.dom.ControllerFile.RequestMap

class RequestUriCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        ProjectServiceInterface ps = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        PsiElement myElement = parameters.getPosition()
        XmlElement myAttrValue
        try {
            myAttrValue = myElement as XmlAttributeValue
        } catch (ClassCastException ignored) {
            myAttrValue = myElement as XmlElement
        }
        DomManager dm = DomManager.getDomManager(myElement.getProject())
        Class clazz
        XmlFile myFile = myAttrValue.getContainingFile() as XmlFile
        if (!myFile) return
        if (dm.getFileElement(myFile, FormFile.class)) {
            clazz = FormFile.class
        } else if (dm.getFileElement(myFile, ScreenFile.class)) {
            clazz = ScreenFile.class
        } else if (dm.getFileElement(myFile, MenuFile.class)) {
            clazz = MenuFile.class
        } else if (dm.getFileElement(myFile, CompoundFile.class)) {
            clazz = CompoundFile.class
        } else {
            return
        }

        String targetType = XmlUtils.getParentTag(myAttrValue)?.getAttribute('target-type')?.getValue()
        if (!targetType) targetType = XmlUtils.getParentTag(myAttrValue)?.getAttribute('url-mode')?.getValue()

        if (targetType && targetType == 'inter-app') {
            Map<String, List<String>> mountPointAndRequestMaps = ps.getAllMountPointsAndRequestMaps(myElement)
            mountPointAndRequestMaps.forEach { String mountPoint, List uris ->
                uris.forEach { uri ->
                    String lookupValue = "$mountPoint/control/$uri"
                    LookupElement lookupElement = LookupElementBuilder.create(lookupValue)
                    result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
                }
            }
        } else {
            String componentName = MiscUtils.getComponentName(myAttrValue, clazz)
            List<RequestMap> uris = ps.getComponentRequestMaps(componentName, myAttrValue.getProject())
            uris.each { RequestMap req ->
                LookupElement lookupElement = LookupElementBuilder.create(req.getUri().getValue())
                        .withTailText(" Component:${MiscUtils.getComponentName(req)}" as String, true)
                result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
            }
        }
    }
}
