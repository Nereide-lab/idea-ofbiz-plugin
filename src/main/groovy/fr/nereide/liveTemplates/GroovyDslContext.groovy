package fr.nereide.liveTemplates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

class GroovyDslContext extends TemplateContextType {

    protected GroovyDslContext() {
        super('Groovy')
    }

    boolean isInContext(TemplateActionContext tac) {
        PsiFile file = tac.getFile()
        return (file instanceof GroovyFile) && (file as GroovyFile).isScript()
    }
}
