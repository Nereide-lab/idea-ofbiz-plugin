package fr.nereide.documentation.format

import com.intellij.openapi.util.text.HtmlBuilder
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.EntityField
import fr.nereide.dom.EntityModelFile.EntityPrimKey
import fr.nereide.dom.EntityModelFile.ExtendEntity
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.MiscUtils

import static com.intellij.lang.documentation.DocumentationMarkup.CONTENT_ELEMENT
import static com.intellij.lang.documentation.DocumentationMarkup.GRAYED_ELEMENT
import static com.intellij.openapi.util.text.HtmlChunk.Element
import static com.intellij.openapi.util.text.HtmlChunk.text
import static fr.nereide.dom.EntityModelFile.EntityRelation

class OfbizEntityDocumentationFormatter extends OfbizCommonDocumentationFormatter {

    static Element formatEntityDefinition(Entity entity) {
        return formatEntityOrViewDefinition(entity)
    }

    static Element formatExtendEntityListForEntity(String entityName, ProjectServiceInterface ps) {
        HtmlBuilder builder = new HtmlBuilder()
        List<ExtendEntity> extendList = ps.getExtendEntityListForEntity(entityName)
        extendList.forEach {
            List<EntityField> extendedFields = it.getFields()
            String fileName = it.getXmlElement().getContainingFile().getName()
            HtmlBuilder extendBuilder = new HtmlBuilder()
                    .append(text("Extended ").bold())
                    .nbsp()
                    .append(text("in file ${fileName} [component ${MiscUtils.getComponentName(it)}]"))
                    .append(text(', with fields: '))
                    .append(formatExtendFields(extendedFields))
            builder.append(extendBuilder)
        }
        return builder.wrapWith(CONTENT_ELEMENT)
    }

    static Element formatEntityFieldList(String entityName, ProjectServiceInterface ps) {
        HtmlBuilder builder = new HtmlBuilder()
        Entity entity = ps.getEntity(entityName)
        List<EntityField> fields = entity.getFields()
        List<EntityPrimKey> pks = entity.getPrimKeys()

        HtmlBuilder pkListBuilder = new HtmlBuilder()
        builder.append(text("Primary keys:").bold()).nbsp()
        fields.stream().filter { isInPkList(pks, it) }
                .forEach {
                    pkListBuilder.append(new HtmlBuilder()
                            .append(text("${it.getName().getValue()}"))
                            .nbsp()
                            .append(text("${it.getType().getValue()}").wrapWith(GRAYED_ELEMENT))
                            .wrapWith('li')
                    )
                }
        return builder.append(pkListBuilder.wrapWith('ul')).wrapWith(CONTENT_ELEMENT)
    }

    static Element formatEntityRelations(String entityName, ProjectServiceInterface ps) {
        HtmlBuilder builder = new HtmlBuilder()
        Entity entity = ps.getEntity(entityName)
        List<EntityRelation> relations = entity.getRelations()

        builder.append(text("Related to:").bold()).nbsp()
        HtmlBuilder relListBuilder = new HtmlBuilder()
        relations.forEach {
            HtmlBuilder relBuilder = new HtmlBuilder()
                    .append(text("${it.getRelEntityName().getValue()}"))
                    .append(text("[${it.getType().getValue()}]").italic().wrapWith(GRAYED_ELEMENT))
            relListBuilder.append(relBuilder.wrapWith('li'))
        }
        return builder.append(relListBuilder).wrapWith(CONTENT_ELEMENT)
    }

    private static boolean isInPkList(List<EntityPrimKey> pks, EntityField it) {
        pks.stream().map { it.getField().getValue() }.collect().contains(it.getName().getValue())
    }

    private static Element formatExtendFields(List<EntityField> extendedFields) {
        HtmlBuilder fieldListBuilder = new HtmlBuilder()
        extendedFields.forEach {
            HtmlBuilder fieldBuilder = new HtmlBuilder()
            fieldBuilder.append(text("${it.getName()}"))
            fieldBuilder.nbsp()
            if (it.getType().getValue() != "") {
                fieldBuilder.append(text("${it.getType().getValue()}").wrapWith(GRAYED_ELEMENT))
            }
            fieldListBuilder.append(fieldBuilder.wrapWith('li'))
        }
        return fieldListBuilder.wrapWith('ul')
    }
}
