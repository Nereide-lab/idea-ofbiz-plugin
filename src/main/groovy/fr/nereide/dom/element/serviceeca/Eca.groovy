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
package fr.nereide.dom.element.serviceeca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute
import fr.nereide.dom.element.commoneca.Action
import fr.nereide.dom.element.commoneca.Condition

/**
 * Part of the OFBiz DOM description
 */
interface Eca extends DomElement {

    @Attribute('service')
    GenericAttributeValue<String> getService()

    @Attribute('event')
    GenericAttributeValue<String> getEvent()

    @SubTagList('condition')
    List<Condition> getConditions()

    @SubTagList('action')
    List<Action> getActions()

}
