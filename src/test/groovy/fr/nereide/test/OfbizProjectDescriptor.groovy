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

package fr.nereide.test

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.IdeaTestUtil
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor
import org.jetbrains.annotations.NotNull

class OfbizProjectDescriptor extends DefaultLightProjectDescriptor {

    @Override
    Sdk getSdk() {
        return IdeaTestUtil.getMockJdk11()
    }

    @Override
    void setUpProject(@NotNull Project project, @NotNull SetupHandler handler) {
        super.setUpProject(project, handler)
        WriteAction.run(() -> {
            def main = ModuleManager.getInstance(project).findModuleByName(TEST_MODULE_NAME)
            PsiTestUtil.addLibrary(main, "lib/ofbiz.jar")
        })
    }
}
