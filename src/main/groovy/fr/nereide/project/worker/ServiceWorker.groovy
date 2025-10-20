package fr.nereide.project.worker

import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.EntityField
import fr.nereide.dom.element.service.Service
import fr.nereide.dom.element.service.ServiceAttribute
import fr.nereide.dom.element.service.ServiceAutoAttribute
import fr.nereide.project.OfbizProjectHelper

/**
 * Various helper methods for services
 */
class ServiceWorker {

    final static String SERVICE_ATTR_NAME = 'attribute-name'
    final static String SERVICE_ATTR_TYPE = 'attribute-type'
    public static final List<String> IN_SERVICE_MODE = ['IN', 'INOUT']
    public static final String TRUE = 'true'
    public static final String FALSE = 'false'

    static List<Map> getRequiredInAttributes(Service service, OfbizProjectHelper ph) {
        return getServiceInAttributes(service, ph, FALSE)
    }

    static List<Map> getOptionalInAttributes(Service service, OfbizProjectHelper ph) {
        return getServiceInAttributes(service, ph, TRUE)
    }

    private static List<Map> getServiceInAttributes(Service service, OfbizProjectHelper ph, String optional) {
        List<ServiceAttribute> serviceAttributes = service.attributes
        List<Map> paramListToReturn = []
        // Vanilla params
        paramListToReturn.addAll(collectParamsInService(serviceAttributes, optional))
        // EntityAuto
        String defaultEntityName = service.defaultEntityName.value
        if (defaultEntityName && service.engine.value == 'entity-auto') {
            Entity usedEntity = ph.getEntity(defaultEntityName)
            List<EntityField> entityFields = usedEntity.fields
            List<String> pkFieldNames = getPkNamesList(usedEntity)
            for (ServiceAutoAttribute saa : service.autoAttributes) {
                if (saa.optional.value == optional) {
                    String includeType = saa.include.value
                    List<EntityField> fieldsToAdd = entityFields.findAll { EntityField field ->
                        includeType == 'pk' ? isEntityPk(pkFieldNames, field) : !isEntityPk(pkFieldNames, field)
                    }
                    fieldsToAdd.forEach { field ->
                        paramListToReturn.add(makeServiceAttrMap(field))
                    }
                }
            }
        }
        return paramListToReturn
    }

    private static List<LinkedHashMap<Object, Object>> collectParamsInService(List<ServiceAttribute> serviceAttributes,
                                                                              String optional) {
        return serviceAttributes.findAll { attr ->
            optional == TRUE ? !isRequiredAttribute(attr) : isRequiredAttribute(attr) &&
                    IN_SERVICE_MODE.contains(attr.mode.value)
        }.collect { attr -> makeServiceAttrMap(attr) }
    }

    private static boolean isEntityPk(List<String> pkFieldNames, EntityField it) {
        return pkFieldNames.contains(it.name.value)
    }

    private static List<String> getPkNamesList(Entity usedEntity) {
        return usedEntity.primKeys.collect { pk -> pk.field.value }
    }

    private static Map makeServiceAttrMap(ServiceAttribute it) {
        Map attrMap = [:]
        attrMap.put(SERVICE_ATTR_NAME, it.name.value)
        attrMap.put(SERVICE_ATTR_TYPE, it.type.value)
        return attrMap
    }

    private static Map makeServiceAttrMap(EntityField it) {
        Map attrMap = [:]
        attrMap.put(SERVICE_ATTR_NAME, it.name.value)
        attrMap.put(SERVICE_ATTR_TYPE, it.type.value)
        return attrMap
    }

    private static boolean isRequiredAttribute(ServiceAttribute it) {
        return !it.optional.value || it.optional.value == FALSE
    }

}
