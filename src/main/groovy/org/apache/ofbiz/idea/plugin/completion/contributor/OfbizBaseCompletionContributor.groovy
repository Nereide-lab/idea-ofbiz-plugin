package org.apache.ofbiz.idea.plugin.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import org.apache.ofbiz.idea.plugin.completion.provider.common.EntityOrViewNameCompletionProvider
import org.apache.ofbiz.idea.plugin.completion.provider.common.ServiceNameCompletionProvider

class OfbizBaseCompletionContributor extends CompletionContributor {
    EntityOrViewNameCompletionProvider entityOrViewNameCompletionProvider
    ServiceNameCompletionProvider serviceNameCompletionProvider

    OfbizBaseCompletionContributor() {
        entityOrViewNameCompletionProvider = new EntityOrViewNameCompletionProvider()
        serviceNameCompletionProvider = new ServiceNameCompletionProvider()
    }
}
