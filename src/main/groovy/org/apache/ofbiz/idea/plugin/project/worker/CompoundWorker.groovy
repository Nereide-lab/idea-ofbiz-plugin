/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.project.worker

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import org.apache.ofbiz.idea.plugin.dom.FormFile

class CompoundWorker {

    static List<DomElement> getDomElementListFromCompound(DomFileElement<DomElement> domFile, String listGetterMethod, Class fileType) {
        List<DomElement> toReturn = []
        if (fileType.isAssignableFrom(FormFile.class)) {
            List elements = domFile.getRootElement().getForms().getForms()
            if (elements) toReturn.addAll(elements)
            elements = domFile.getRootElement().getForms().getGrids()
            if (elements) toReturn.addAll(elements)
        } else {
            toReturn.addAll(domFile.getRootElement()."$listGetterMethod"()."$listGetterMethod"())
        }
        toReturn
    }
}
