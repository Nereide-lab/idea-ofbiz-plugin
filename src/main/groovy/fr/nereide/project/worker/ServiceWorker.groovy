package fr.nereide.project.worker

import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.EntityField
import fr.nereide.dom.element.service.Service
import fr.nereide.dom.element.service.ServiceAttribute
import fr.nereide.dom.element.service.ServiceAutoAttribute
import fr.nereide.project.OfbizProjectHelper

class ServiceWorker {
    final static String SERVICE_ATTR_NAME = 'attribute-name'
    final static String SERVICE_ATTR_TYPE = 'attribute-type'
    public static final ArrayList<String> IN_SERVICE_MODE = ['IN', 'INOUT']

    static List<Map> getRequiredInAttributes(Service service, OfbizProjectHelper ph) {
        return getServiceInAttributes(service, ph, 'false')
    }

    static List<Map> getOptionalInAttributes(Service service, OfbizProjectHelper ph) {
        return getServiceInAttributes(service, ph, 'true')
    }

    private static ArrayList<Map> getServiceInAttributes(Service service, OfbizProjectHelper ph, String optional) {
        List<ServiceAttribute> serviceAttributes = service.attributes
        List<Map> paramListToReturn = []
        // Vanilla params
        paramListToReturn.addAll(serviceAttributes.stream()
                .filter { optional == 'true' ? !isRequiredAttribute(it) : isRequiredAttribute(it) }
                .filter { IN_SERVICE_MODE.contains(it.mode.value) }
                .map { makeServiceAttrMap(it) }
                .collect())
        //EntityAuto
        String defaultEntityName = service.defaultEntityName.value
        if (defaultEntityName && service.engine.value == 'entity-auto') {
            Entity usedEntity = ph.getEntity(defaultEntityName)
            List<EntityField> entityFields = usedEntity.fields
            List<String> pkFieldNames = getPkNamesList(usedEntity)
            for (ServiceAutoAttribute saa : service.autoAttributes) {
                if (saa.optional.value == optional) {
                    String includeType = saa.include.value
                    List<EntityField> fieldsToAdd = entityFields.findAll { EntityField it ->
                        includeType == 'pk' ? isEntityPk(pkFieldNames, it) : !isEntityPk(pkFieldNames, it)
                    }
                    fieldsToAdd.forEach {
                        paramListToReturn.add(makeServiceAttrMap(it))
                    }
                }
            }
        }
        return paramListToReturn
    }

    private static boolean isEntityPk(List<String> pkFieldNames, EntityField it) {
        return pkFieldNames.contains(it.name.value)
    }

    private static Collection getPkNamesList(Entity usedEntity) {
        return usedEntity.primKeys.stream().map { it.field.value }.collect()
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
        return !it.optional.value || it.optional.value == 'false'
    }

}
