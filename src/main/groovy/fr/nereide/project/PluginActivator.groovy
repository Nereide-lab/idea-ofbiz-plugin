package fr.nereide.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
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

        JavaPsiFacade jpf = JavaPsiFacade.getInstance(project)
        return jpf.findPackage(OfbizPluginConstants.BASE_OFB_PACKAGE) != null
    }

}
