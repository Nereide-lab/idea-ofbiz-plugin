package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import fr.nereide.completion.provider.common.EntityOrViewNameCompletionProvider
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

class EntityNameCompletionProvider extends EntityOrViewNameCompletionProvider {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        addEntitiesLookup(structureService, result)
    }
}