/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
    final static String INOUT = 'INOUT'
    public static final List<String> IN_SERVICE_MODE = ['IN', INOUT]
    public static final List<String> OUT_SERVICE_MODE = ['OUT', INOUT]
    public static final String TRUE = 'true'
    public static final String FALSE = 'false'

    static List<Map> getRequiredInParameters(Service service, OfbizProjectHelper ph) {
        return getServiceInParameters(service, ph, FALSE)
    }

    static List<Map> getOptionalInParameters(Service service, OfbizProjectHelper ph) {
        return getServiceInParameters(service, ph, TRUE)
    }

    static List<Map> getServiceOutParameters(OfbizProjectHelper ph, String serviceName, String optional) {
        return getServiceParameters(ph.getService(serviceName), ph, optional, OUT_SERVICE_MODE)
    }

    private static List<Map> getServiceInParameters(Service service, OfbizProjectHelper ph, String optional) {
        return getServiceParameters(service, ph, optional, IN_SERVICE_MODE)
    }

    private static List<Map> getServiceParameters(Service service, OfbizProjectHelper ph, String optional, List modes) {
        List<ServiceAttribute> serviceAttributes = service.attributes
        List<Map> paramListToReturn = []
        // Vanilla params
        paramListToReturn.addAll(collectParamsInService(serviceAttributes, optional, modes))
        // Implements
        paramListToReturn.addAll(getParamsFromServiceImplement(ph, service, optional, modes))
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

    private static List<Map> getParamsFromServiceImplement(OfbizProjectHelper ph, Service service, String optional,
                                                           List<String> modes) {
        if (service.implements.size() == 0) {
            return []
        }
        List<Service> implementedServices = service.implements.service*.value.collect {
            serviceName -> ph.getService(serviceName)
        }
        return implementedServices.collect { Service implementedService ->
            getServiceParameters(implementedService, ph, optional, modes)
        }.flatten() as List<Map>
    }

    private static List<LinkedHashMap<Object, Object>> collectParamsInService(List<ServiceAttribute> serviceAttributes,
                                                                              String optional, List<String> modes) {
        return serviceAttributes
                .findAll { attr -> modes.contains(attr.mode.value) }
                .findAll { attr -> optional == TRUE ? !isRequiredAttribute(attr) : isRequiredAttribute(attr) }
                .collect { attr -> makeServiceAttrMap(attr) }
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
