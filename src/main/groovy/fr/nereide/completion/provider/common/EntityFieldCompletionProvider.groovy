package fr.nereide.completion.provider.common

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.find.FindManager
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.find.impl.FindManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import com.intellij.util.ArrayUtil
import com.intellij.util.CommonProcessors
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
        String entityName
        try {
            entityName = getEntityNameFromPsiElement(parameters.getPosition())
        } catch (Exception e) {
            e.printStackTrace()
            return
        }
        if (!entityName) return
        if (entityName.startsWith('\"')) entityName = entityName.replaceAll('\"', '')
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

    /**
     * Add lookup element to display, with the fields and the entity name at the end of the line
     * @param result
     * @param fields
     * @param entityName
     */
    static addLookupElementFromEntity(CompletionResultSet result, List<String> fields, String entityName) {
        fields.forEach { field ->
            result.addElement(PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(field).withTypeText("From $entityName" as String),
                    1000))
        }
    }

    /**
     * Extracts entity name from declarations like
     * <code> EntityQuery.use(delegator).from() </code>
     * @param declaration
     * @return
     */
    static String getEntityNameFromDeclarationString(String declaration) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declaration)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

    /**
     * returns the list of all usages of the variable
     * @param variable
     * @return
     */
    static List<UsageInfo> getUsagesOfVariable(PsiVariable variable) {
        Project project = variable.getProject()
        FindUsagesHandler handler = ((FindManagerImpl) FindManager.getInstance(project))
                .getFindUsagesManager()
                .getFindUsagesHandler(variable, false)
        if (!handler) return null
        CommonProcessors.CollectProcessor<UsageInfo> processor = new CommonProcessors.CollectProcessor<>(Collections.synchronizedList(new ArrayList<>()))
        if (!processor) return null
        PsiElement[] psiElements = ArrayUtil.mergeArrays(handler.getPrimaryElements(), handler.getSecondaryElements())
        FindUsagesOptions options = handler.getFindUsagesOptions(null)
        for (PsiElement psiElement : psiElements) {
            handler.processElementUsages(psiElement, processor, options)
        }
        return processor.getResults()
    }

    /**
     * Try to get the EntityName from PsiElement and its context
     * @param psiElement
     * @return
     */
    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

}
