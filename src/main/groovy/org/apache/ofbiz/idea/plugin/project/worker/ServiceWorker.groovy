package org.apache.ofbiz.idea.plugin.project.worker

import com.intellij.util.xml.DomElement
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.Entity
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.EntityField
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface

class ServiceWorker {
    final static String SERVICE_ATTR_NAME = 'attribute-name'
    final static String SERVICE_ATTR_TYPE = 'attribute-type'
    public static final ArrayList<String> IN_SERVICE_MODE = ['IN', 'INOUT']

    static List<Map> getRequiredInAttributes(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service, ProjectServiceInterface ps) {
        return getServiceInAttributes(service, ps, 'false')
    }

    static List<Map> getOptionalInAttributes(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service, ProjectServiceInterface ps) {
        return getServiceInAttributes(service, ps, 'true')
    }

    private static ArrayList<Map> getServiceInAttributes(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service, ProjectServiceInterface ps, String optional) {
        List<org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.ServiceAttribute> serviceAttributes = service.getAttributes()
        List<Map> paramListToReturn = []
        // Vanilla params
        paramListToReturn.addAll(serviceAttributes.stream()
                .filter { optional == 'true' ? !isRequired(it) : isRequired(it) }
                .filter { IN_SERVICE_MODE.contains(it.getMode().getValue()) }
                .map { makeServiceAttrMap(it) }
                .collect())
        //EntityAuto
        String defaultEntityName = service.getDefaultEntityName().getValue()
        if (defaultEntityName && service.getEngine().getValue() == 'entity-auto') {
            Entity usedEntity = ps.getEntity(defaultEntityName)
            List<EntityField> entityFields = usedEntity.getFields()
            List<String> pkFieldNames = getPkNamesList(usedEntity)
            for (org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.ServiceAutoAttribute saa : service.getAutoAttributes()) {
                if (saa.getOptional().getValue() == optional) {
                    String includeType = saa.getInclude().getValue()
                    List<org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.ServiceAttribute> fieldsToAdd = entityFields.stream().filter { it ->
                        includeType == 'pk' ? isEntityPk(pkFieldNames, it) : !isEntityPk(pkFieldNames, it)
                    }.collect()
                    fieldsToAdd.forEach {
                        paramListToReturn.add(makeServiceAttrMap(it))
                    }
                }
            }
        }
        return paramListToReturn
    }

    private static boolean isEntityPk(List<String> pkFieldNames, EntityField it) {
        return pkFieldNames.contains(it.getName().getValue())
    }

    private static Collection getPkNamesList(Entity usedEntity) {
        return usedEntity.getPrimKeys().stream().map { it.getField().getValue() }.collect()
    }

    private static Map makeServiceAttrMap(DomElement it) {
        Map attrMap = [:]
        attrMap.put(SERVICE_ATTR_NAME, it.getName().getValue())
        attrMap.put(SERVICE_ATTR_TYPE, it.getType().getValue())
        return attrMap
    }

    private static boolean isRequired(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.ServiceAttribute it) {
        return !it.getOptional().getValue() || it.getOptional().getValue() == 'false'
    }

}
