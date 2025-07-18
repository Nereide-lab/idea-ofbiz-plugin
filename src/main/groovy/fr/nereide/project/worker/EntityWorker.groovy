package fr.nereide.project.worker

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.xml.DomElement
import fr.nereide.dom.element.entitymodel.*
import fr.nereide.project.OfbizProjectHelper

class EntityWorker {

    private static final Logger LOG = Logger.getInstance(EntityWorker.class)
    /**
     * Returns a String list of all entity fields
     * @param entity
     * @return
     */
    static List<String> getEntityFields(Entity entity, OfbizProjectHelper ph) {
        return getEntityFields(entity, null, [], ph)
    }

    static List<String> getEntityFields(Entity entity, String prefix, List<String> excludedFields, OfbizProjectHelper ph) {
        List<EntityField> fields = entity.getFields().findAll { entityField ->
            !excludedFields.contains(entityField.getName().getValue())
        }
        List<ExtendEntity> extendList = ph.getExtendEntityListForEntity(entity.entityName.value)
        if (extendList && extendList.size() > 0) {
            fields.addAll(extendList.collect { it.getFields() })
        }
        return getFormatedFieldsName(fields, prefix)
    }

    /**
     * Returns a String list of all entity fields with prefixes, and nested views included in the search
     * @param view
     * @param ph
     * @param index
     * @return
     */
    static List<String> getViewFields(ViewEntity view, OfbizProjectHelper ph, int index) {
        return getViewFields(view, null, ph, [], index)
    }

    static List<String> getViewFields(ViewEntity view, String prefix, OfbizProjectHelper ph,
                                      List<String> excludedFields, int index) {
        List<String> fieldsList = []
        if (index >= 10) return // infinite loop workaround
        List<Alias> aliases = view.aliases
        List<AliasAll> aliasAllList = view.aliasAlls
        if (aliasAllList) {
            List<ViewEntityMember> members = view.memberEntities
            aliasAllList.each { aliasAllElmt ->
                String currentPrefix = "${prefix ?: ''}${aliasAllElmt.prefix.value ?: ''}" as String
                String entityName = getEntityNameFromAlias(aliasAllElmt, members)
                if (entityName) {
                    List<String> currentExcludedFields = getListOfExcludedFieldNames(aliasAllElmt)
                    if (currentExcludedFields) currentExcludedFields.addAll(excludedFields)
                    Entity currentEntity = ph.getEntity(entityName)
                    if (currentEntity) {
                        fieldsList.addAll(getEntityFields(currentEntity, currentPrefix, currentExcludedFields, ph))
                    } else {
                        ViewEntity currentView = ph.getViewEntity(entityName)
                        List<String> viewFields = getViewFields(currentView, currentPrefix, ph, currentExcludedFields, index + 1)
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
        return members.find { it.entityAlias.value == alias }?.entityName
    }

    static List<String> getListOfExcludedFieldNames(AliasAll aliasAllElmt) {
        return aliasAllElmt.getAliasAllExcludes().collect { it.field.value }
    }


    static List<String> getFormatedFieldsName(List<Alias> aliases) {
        return getFormatedFieldsName(aliases, null)
    }

    static List<String> getFormatedFieldsName(List<DomElement> fields, String prefix) {
        return fields.collect { DomElement field ->
            "${prefix ?: ''}${field.getName()}"
        } as List<String>
    }

    static boolean entityOrViewHasNeverCacheTrueAttr(String entityName, Project project) {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(project)
        Entity entity = ph.getEntity(entityName)
        if (!entity) {
            ViewEntity view = ph.getViewEntity(entityName)
            if (!view) return false
            List<ViewEntityMember> members = view.getMemberEntities()
            return members.any {
                entityOrViewHasNeverCacheTrueAttr(it.getEntityName().getValue(), project)
            }
        }
        String neverCache = entity.neverCache ?: ''
        return neverCache ? neverCache == 'true' : false
    }

}
