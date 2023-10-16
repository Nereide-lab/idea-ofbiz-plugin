package fr.nereide.project.worker

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.xml.DomElement
import fr.nereide.project.ProjectServiceInterface

import static fr.nereide.dom.EntityModelFile.*

class EntityWorker {

    private static final Logger LOG = Logger.getInstance(EntityWorker.class)
    /**
     * Returns a String list of all entity fields
     * @param entity
     * @return
     */
    static List<String> getEntityFields(Entity entity) {
        return getEntityFields(entity, null, [])
    }

    static List<String> getEntityFields(Entity entity, String prefix, List<String> excludedFields) {
        List<EntityField> fields = entity.getFields().findAll { entityField ->
            !excludedFields.contains(entityField.getName().getValue())
        }
        return getFormatedFieldsName(fields, prefix)
    }

    /**
     * Returns a String list of all entity fields with prefixes, and nested views included in the search
     * @param view
     * @param structureService
     * @param index
     * @return
     */
    static List<String> getViewFields(ViewEntity view, ProjectServiceInterface structureService, int index) {
        return getViewFields(view, null, structureService, [], index)
    }

    static List<String> getViewFields(ViewEntity view, String prefix, ProjectServiceInterface structureService,
                                      List<String> excludedFields, int index) {
        List<String> fieldsList = []
        if (index >= 10) return // infinite loop workaround
        List<Alias> aliases = view.getAliases()
        List<AliasAll> aliasAllList = view.getAliasAllList()
        if (aliasAllList) {
            List<ViewEntityMember> members = view.getMemberEntities()
            aliasAllList.each { aliasAllElmt ->
                String currentPrefix = "${prefix ?: ''}${aliasAllElmt.getPrefix().getValue() ?: ''}" as String
                String entityName = getEntityNameFromAlias(aliasAllElmt, members)
                if (entityName) {
                    List<String> currentExcludedFields = getListOfExcludedFieldNames(aliasAllElmt)
                    if (currentExcludedFields) currentExcludedFields.addAll(excludedFields)
                    Entity currentEntity = structureService.getEntity(entityName)
                    if (currentEntity) {
                        fieldsList.addAll(getEntityFields(currentEntity, currentPrefix, currentExcludedFields))
                    } else {
                        ViewEntity currentView = structureService.getViewEntity(entityName)
                        List<String> viewFields = getViewFields(currentView, currentPrefix, structureService, currentExcludedFields, index + 1)
                        if (viewFields) fieldsList.addAll(viewFields)
                    }
                }
            }
        }
        fieldsList.addAll(getFormatedFieldsName(aliases))
        return fieldsList.unique()
    }

    static String getEntityNameFromAlias(AliasAll aliasAllElmt, List<ViewEntityMember> members) {
        String alias = aliasAllElmt.getEntityAlias()
        return members.find { it.getEntityAlias().getValue() == alias }?.getEntityName()
    }

    static List<String> getListOfExcludedFieldNames(AliasAll aliasAllElmt) {
        return aliasAllElmt.getAliasAllExcludes().collect { it.getField().getValue() }
    }


    static List<String> getFormatedFieldsName(aliases) {
        return getFormatedFieldsName(aliases, null)
    }

    static List<String> getFormatedFieldsName(List<DomElement> fields, String prefix) {
        return fields.stream().map { DomElement field ->
            "${prefix ?: ''}${field.getName()}"
        }.toList() as List<String>
    }

    static boolean entityOrViewHasNeverCacheTrueAttr(String entityName, Project project) {
        ProjectServiceInterface service = project.getService(ProjectServiceInterface.class)
        Entity entity = service.getEntity(entityName)
        if (!entity) {
            ViewEntity view = service.getViewEntity(entityName)
            if (!view) return false
            List<ViewEntityMember> members = view.getMemberEntities()
            return members.any {
                entityOrViewHasNeverCacheTrueAttr(it.getEntityName().getValue(), project)
            }
        }
        String neverCache = entity.getNeverCache() ?: ''
        return neverCache ? neverCache == 'true' : false
    }

}
