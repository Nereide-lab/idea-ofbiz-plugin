package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.NotNull

import static fr.nereide.dom.ScreenFile.Screen

class ScreenNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        List<Screen> screens = structureService.getAllScreens()
        screens.forEach { screen ->
            LookupElement lookupElement = LookupElementBuilder.create(screen.getName().getValue())
                    .withTailText(" Component:${MiscUtils.getComponentName(screen)}" as String, true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, 100))
        }
    }
}
