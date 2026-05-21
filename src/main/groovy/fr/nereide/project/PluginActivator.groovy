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
package fr.nereide.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import fr.nereide.project.pattern.OfbizPluginConstants
import org.jetbrains.annotations.NotNull

/**
 * Class used to see if the plugin should be enabled on a project.
 * Checked in the beginning of each process, because the idea plugin framework
 * doesn't offer an activator feature like Eclipse plugins
 */
@Service(Service.Level.PROJECT)
final class PluginActivator {

    private final Project project
    private Boolean isOfbizProject = null

    static PluginActivator getInstance(Project project) {
        return project.getService(PluginActivator)
    }

    PluginActivator(@NotNull Project project) {
        this.project = project
    }

    boolean isInactive() {
        return !active
    }

    boolean isActive() {
        if (isOfbizProject != null) return isOfbizProject
        this.isOfbizProject = shouldActivate()
        return this.isOfbizProject
    }

    private boolean shouldActivate() {
        if (ApplicationManager.application.unitTestMode) {
            return true
        }
        return OfbizClassUtil.findPackage(project, OfbizPluginConstants.BASE_OFB_PACKAGE)
    }

}
