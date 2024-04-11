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

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTagList

interface ComponentFile extends DomElement {
    @Attribute("name")
    @Stubbed
    GenericAttributeValue<String> getName()

    @SubTagList("entity-resource")
    List<EntityModelFile> getEntityResources()

    @SubTagList("service-resource")
    List<ServiceDefFile> getServiceResources()

    @SubTagList("webapp")
    List<Webapp> getWebapps()

    interface Webapp extends DomElement {
        @NameValue
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("title")
        GenericAttributeValue<String> getTitle()

        @Attribute("server")
        GenericAttributeValue<String> getServer()

        @Attribute("location")
        GenericAttributeValue<String> getLocation()

        @Attribute("base-permission")
        GenericAttributeValue<String> getBasePermission()

        @Attribute("app-shortcut-screen")
        GenericAttributeValue<String> getAppShortcutScreen()

        @Attribute("mount-point")
        GenericAttributeValue<String> getMountPoint()
    }
}
