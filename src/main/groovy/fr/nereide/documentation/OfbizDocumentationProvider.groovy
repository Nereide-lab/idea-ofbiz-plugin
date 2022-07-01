/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.nereide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import fr.nereide.dom.ServiceDefFile
import fr.nereide.project.ProjectServiceInterface

import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_END
import static com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_START

class OfbizDocumentationProvider extends AbstractDocumentationProvider {

    static String generateServiceHoverDoc(String serviceName, ProjectServiceInterface ps) {
        HtmlBuilder doc = new HtmlBuilder()
        ServiceDefFile.Service service = ps.getService(serviceName)


        // titre et définition
        doc.append(DEFINITION_START)
        doc.append("<service name=\"${service.getName()}\" engine=\"${service.getEngine()} auth=\"${service.getAuth()}\"" +
                "location=\"${service.getLocation()}\" invoke=\"${service.getInvoke()}\">")
        doc.append(HtmlChunk.br())
        doc.append("<description>${service.getDescription() ?: "No description found"}</description>")
        doc.append(DEFINITION_END)

        return doc
    }
}
