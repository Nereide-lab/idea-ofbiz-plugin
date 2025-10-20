package fr.nereide.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import fr.nereide.completion.provider.common.EntityOrViewNameCompletionProvider
import fr.nereide.completion.provider.common.ServiceNameCompletionProvider

/**
 * Part of the OFBiz plugin Completion system
 */
class BaseCompletionContributor extends CompletionContributor {

    EntityOrViewNameCompletionProvider entityOrViewNameCompletionProvider
    ServiceNameCompletionProvider serviceNameCompletionProvider

    BaseCompletionContributor() {
        entityOrViewNameCompletionProvider = new EntityOrViewNameCompletionProvider()
        serviceNameCompletionProvider = new ServiceNameCompletionProvider()
    }

}
