package fr.nereide.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import fr.nereide.dom.EntityModelFile
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.worker.EntityWorker
import org.jetbrains.annotations.NotNull

import java.util.regex.Matcher
import java.util.regex.Pattern

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType
import static fr.nereide.dom.EntityModelFile.Entity

class JavaEntityFieldsCompletionProvider extends CompletionProvider<CompletionParameters> {
    private static final Pattern ENTITY_NAME_PATTERN = Pattern.compile("(['\"](.*?)['\"])")

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        ProjectServiceInterface ps = parameters.getPosition().getProject().getService(ProjectServiceInterface.class)
        PsiElement element = parameters.getPosition()
        Entity entity
        String entityName = getEntityNameFromElement(element)
        if (!entityName) return
        entity = ps.getEntity(entityName)
        if (entity) {
            List<String> entityFields = EntityWorker.getEntityFields(entity)
            entityFields.forEach { field ->
                result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(field), 1000))
            }
        } else {
            EntityModelFile.ViewEntity view = ps.getViewEntity(entityName)
            List<String> viewFields = EntityWorker.getViewFields(view, ps, 0)
            viewFields.forEach { field ->
                result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(field), 1000))
            }
        }
    }

    private static String getEntityNameFromElement(PsiElement element) {
        PsiMethodCallExpression fullGetStatement = getParentOfType(element, PsiMethodCallExpression.class)
        List<PsiReferenceExpression> fullGetStatementParts = getChildrenOfTypeAsList(fullGetStatement,
                PsiReferenceExpression.class)

        List<PsiReferenceExpression> subGetStatementParts = getChildrenOfTypeAsList((fullGetStatementParts[0] as PsiElement),
                PsiReferenceExpression.class)

        PsiVariable gvVariable = subGetStatementParts[0].resolve() as PsiVariable
        PsiExpression init = gvVariable.getInitializer()
        if (init) {
            return getEntityNameFromDeclarationString(init.text)
        } else {
            PsiForeachStatement basicFor = getParentOfType(element, PsiForeachStatement.class)
            if (basicFor) {
                PsiReferenceExpression iteratedValue = basicFor.getIteratedValue() as PsiReferenceExpression
                if (iteratedValue) {
                    PsiVariable iteratedValueVariable = iteratedValue.resolve() as PsiVariable
                    return getEntityNameFromDeclarationString(iteratedValueVariable.getInitializer().text)
                }
            }
        }
        return null
    }

    static String getEntityNameFromDeclarationString(String declaration) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declaration)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

}
