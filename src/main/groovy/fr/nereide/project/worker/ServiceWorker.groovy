package fr.nereide.project.worker

import com.intellij.util.xml.DomElement
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.EntityField
import fr.nereide.dom.ServiceDefFile.ServiceAttribute
import fr.nereide.dom.ServiceDefFile.Service
import fr.nereide.dom.ServiceDefFile.ServiceAutoAttribute
import fr.nereide.project.ProjectServiceInterface

class ServiceWorker {
    final static String SERVICE_ATTR_NAME = 'attribute-name'
    final static String SERVICE_ATTR_TYPE = 'attribute-type'
    public static final ArrayList<String> IN_SERVICE_MODE = ['IN', 'INOUT']

    static List<Map> getRequiredInAttributes(Service service, ProjectServiceInterface ps) {
        return getServiceInAttributes(service, ps, 'false')
    }

    static List<Map> getOptionalInAttributes(Service service, ProjectServiceInterface ps) {
        return getServiceInAttributes(service, ps, 'true')
    }

    private static ArrayList<Map> getServiceInAttributes(Service service, ProjectServiceInterface ps, String optional) {
        List<ServiceAttribute> serviceAttributes = service.getAttributes()
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
            for (ServiceAutoAttribute saa : service.getAutoAttributes()) {
                if (saa.getOptional().getValue() == optional) {
                    String includeType = saa.getInclude().getValue()
                    List<ServiceAttribute> fieldsToAdd = entityFields.stream().filter { it ->
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

    private static boolean isRequired(ServiceAttribute it) {
        return !it.getOptional().getValue() || it.getOptional().getValue() == 'false'
    }

}
