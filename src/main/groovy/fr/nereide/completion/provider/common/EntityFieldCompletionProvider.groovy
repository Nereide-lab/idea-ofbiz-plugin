package fr.nereide.completion.provider.common

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

import java.util.regex.Matcher
import java.util.regex.Pattern

import static fr.nereide.dom.EntityModelFile.Entity
import static fr.nereide.dom.EntityModelFile.ViewEntity
import static fr.nereide.project.worker.EntityWorker.getEntityFields
import static fr.nereide.project.worker.EntityWorker.getViewFields

abstract class EntityFieldCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final Pattern ENTITY_NAME_PATTERN = Pattern.compile("(['\"](.*?)['\"])")

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        ProjectServiceInterface structureService = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        // Todo : ajouter un try seulement ici
        // Ca permetra d'aérer le code des classes filles
        String entityName = getEntityNameFromPsiElement(parameters.getPosition())

        Entity entity = structureService.getEntity(entityName)

        if (entity) {
            List<String> entityFields = getEntityFields(entity)
            addLookupElementFromEntity(result, entityFields, entityName)
        } else {
            ViewEntity view = structureService.getViewEntity(entityName)
            List<String> viewFields = getViewFields(view, structureService, 0)
            addLookupElementFromEntity(result, viewFields, entityName)
        }
    }

    static addLookupElementFromEntity(CompletionResultSet result, List<String> fields, String entityName) {
        fields.forEach { field ->
            result.addElement(PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(field).withTypeText("From $entityName" as String),
                    1000))
        }
    }

    static String getEntityNameFromDeclarationString(String declaration) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declaration)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

}
