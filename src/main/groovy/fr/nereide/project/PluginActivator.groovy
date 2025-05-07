package fr.nereide.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import fr.nereide.project.pattern.OfbizPatternConst
import org.jetbrains.annotations.NotNull

@Service(Service.Level.PROJECT)
final class PluginActivator {

    private final Project project
    private Boolean isOfbizProject = null

    static PluginActivator getInstance(Project project) {
        return project.getService(PluginActivator.class)
    }

    PluginActivator(@NotNull Project project) {
        this.project = project
    }

    boolean isActive() {
        if (isOfbizProject != null) return isOfbizProject
        this.isOfbizProject = shouldActivate()
        return this.isOfbizProject
    }

    private boolean shouldActivate() {
        if (ApplicationManager.application.isUnitTestMode()) {
            return true
        }

        JavaPsiFacade jpf = JavaPsiFacade.getInstance(project)
        return jpf.findPackage(OfbizPatternConst.BASE_OFB_PACKAGE) != null
    }
}
