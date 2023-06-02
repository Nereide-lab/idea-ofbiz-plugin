package org.apache.ofbiz.idea.plugin.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class EntityNameCompletionProvider extends org.apache.ofbiz.idea.plugin.completion.provider.common.EntityOrViewNameCompletionProvider {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface.class)
        addEntitiesLookup(structureService, result)
    }
}