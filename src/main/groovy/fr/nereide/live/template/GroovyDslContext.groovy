package fr.nereide.live.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

/**
 * Idea context for GDSL
 */
class GroovyDslContext extends TemplateContextType {

    protected GroovyDslContext() {
        super('Groovy')
    }

    boolean isInContext(TemplateActionContext tac) {
        PsiFile file = tac.file
        return (file instanceof GroovyFile) && (file as GroovyFile).script
    }

}
