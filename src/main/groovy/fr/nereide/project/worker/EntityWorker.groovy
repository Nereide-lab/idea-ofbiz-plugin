package fr.nereide.project.worker

import com.intellij.openapi.project.Project
import com.intellij.util.xml.DomElement
import fr.nereide.dom.element.entitymodel.Alias
import fr.nereide.dom.element.entitymodel.AliasAll
import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.EntityField
import fr.nereide.dom.element.entitymodel.ExtendEntity
import fr.nereide.dom.element.entitymodel.ViewEntity
import fr.nereide.dom.element.entitymodel.ViewEntityMember
import fr.nereide.project.OfbizProjectHelper

/**
 * Helper class for entity and views related operations
 */
class EntityWorker {

    public static final String TRUE = 'true'

    /**
     * Returns a String list of all entity fields
     */
    static List<String> getEntityFields(Entity entity, OfbizProjectHelper ph) {
        return getEntityFields(entity, null, [], ph)
    }

    static List<String> getEntityFields(Entity entity, String prefix, List<String> excludedFields,
                                        OfbizProjectHelper ph) {
        List<EntityField> fields = entity.fields.findAll { entityField ->
            !excludedFields.contains(entityField.name.value)
        }
        List<ExtendEntity> extendList = ph.getExtendEntityListForEntity(entity.entityName.value)
        if (extendList && extendList.size() > 0) {
            fields.addAll(extendList*.fields)
        }
        return getFormatedFieldsName(fields, prefix)
    }

    /**
     * Returns a String list of all entity fields with prefixes, and nested views included in the search
     */
    static List<String> getViewFields(ViewEntity view, OfbizProjectHelper ph, int index) {
        return getViewFields(view, null, ph, [], index)
    }

    static List<String> getViewFields(ViewEntity view, String prefix, OfbizProjectHelper ph,
                                      List<String> excludedFields, int index) {
        List<String> fieldsList = []
        if (index >= 10) return [] // infinite loop workaround
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
                        List<String> viewFields =
                                getViewFields(currentView, currentPrefix, ph, currentExcludedFields, index + 1)
                        if (viewFields) fieldsList.addAll(viewFields)
                    }
                }
            }
        }
        fieldsList.addAll(getFormatedFieldsName(aliases))
        return fieldsList.unique()
    }

    static String getEntityNameFromAlias(AliasAll aliasAllElmt, List<ViewEntityMember> members) {
        String alias = aliasAllElmt.entityAlias
        return members.find { member -> member.entityAlias.value == alias }?.entityName
    }

    static List<String> getListOfExcludedFieldNames(AliasAll aliasAllElmt) {
        return aliasAllElmt.aliasAllExcludes.collect { exclude -> exclude.field.value }
    }

    static List<String> getFormatedFieldsName(List<Alias> aliases) {
        return getFormatedFieldsName(aliases, null)
    }

    static List<String> getFormatedFieldsName(List<DomElement> fields, String prefix) {
        return fields.collect { DomElement field ->
            "${prefix ?: ''}${field.name}"
        } as List<String>
    }

    static boolean entityOrViewHasNeverCacheTrueAttr(String entityName, Project project) {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(project)
        Entity entity = ph.getEntity(entityName)
        if (!entity) {
            ViewEntity view = ph.getViewEntity(entityName)
            if (!view) return false
            String neverCache = view.neverCache ?: ''
            return neverCache ? neverCache == TRUE : false
        }
        String neverCache = entity.neverCache ?: ''
        return neverCache ? neverCache == TRUE : false
    }

}
