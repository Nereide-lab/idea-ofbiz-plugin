package fr.nereide.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import fr.nereide.completion.provider.common.EntityOrViewNameCompletionProvider
import fr.nereide.completion.provider.common.ServiceNameCompletionProvider

class OfbizBaseCompletionContributor extends CompletionContributor {
    EntityOrViewNameCompletionProvider entityOrViewNameCompletionProvider
    ServiceNameCompletionProvider serviceNameCompletionProvider

    OfbizBaseCompletionContributor() {
        entityOrViewNameCompletionProvider = new EntityOrViewNameCompletionProvider()
        serviceNameCompletionProvider = new ServiceNameCompletionProvider()
    }
}
