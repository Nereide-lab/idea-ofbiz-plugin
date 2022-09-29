/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

interface ServiceDefFile extends DomElement {

    @SubTagList("service")
    List<Service> getServices()

    interface Service extends DomElement {

        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("engine")
        GenericAttributeValue<String> getEngine()

        @Attribute("location")
        GenericAttributeValue<String> getLocation()

        @Attribute("invoke")
        GenericAttributeValue<String> getInvoke()

        @Attribute("auth")
        GenericAttributeValue<String> getAuth()

        @Attribute("default-entity-name")
        GenericAttributeValue<String> getDefaultEntityName()

        @SubTagList("implements")
        List<ServiceImplements> getImplements()

        @SubTag("description")
        GenericDomValue<String> getDescription()

        @SubTagList("attribute")
        List<ServiceAttribute> getAttributes()

        @SubTagList("auto-attributes")
        List<ServiceAutoAttribute> getAutoAttributes()

        @SubTag("group")
        ServiceGroup getGroup()
    }

    interface ServiceGroup extends DomElement {

        @SubTagList("invoke")
        List<ServiceGroupInvoke> getInvokes()
    }

    interface ServiceAutoAttribute extends DomElement {

        @Attribute('mode')
        GenericAttributeValue<String> getMode()

        @Attribute('include')
        GenericAttributeValue<String> getInclude()

        @Attribute('optional')
        GenericAttributeValue<String> getOptional()

//        @Attribute('entity-name')
//        GenericDomValue<String> getEntityName()
    }

    interface ServiceAttribute extends DomElement {

        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("type")
        GenericAttributeValue<String> getType()

        @Attribute("optional")
        GenericAttributeValue<String> getOptional()

        @Attribute("mode")
        GenericAttributeValue<String> getMode()

        @Attribute("default-value")
        GenericAttributeValue<String> getDefaultValue()
    }

    interface ServiceImplements extends DomElement {

        @NameValue
        @Stubbed
        @Attribute("service")
        GenericAttributeValue<String> getService()

        @Attribute("optional")
        GenericAttributeValue<String> getOptional()
    }

    interface ServiceGroupInvoke extends DomElement {

        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("parameters")
        GenericAttributeValue<String> getParameters()

        @Attribute("result-to-context")
        GenericAttributeValue<String> getResultToContext()

        @Attribute("mode")
        GenericAttributeValue<String> getMode()
    }
}