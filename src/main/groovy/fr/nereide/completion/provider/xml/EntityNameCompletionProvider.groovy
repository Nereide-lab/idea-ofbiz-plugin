package fr.nereide.completion.provider.xml

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import fr.nereide.completion.provider.common.EntityOrViewNameCompletionProvider
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin completion system
 */
class EntityNameCompletionProvider extends EntityOrViewNameCompletionProvider {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        addEntitiesLookup(ph, result)
    }

}
