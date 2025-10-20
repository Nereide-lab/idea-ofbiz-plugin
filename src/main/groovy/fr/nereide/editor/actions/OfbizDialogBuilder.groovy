package fr.nereide.editor.actions

import com.intellij.openapi.project.Project
import fr.nereide.editor.OfbizEditorBundle

/**
 * Base Dialog builder for ofbiz plugin
 */
class OfbizDialogBuilder {

    Project myProject
    Map<String, Object> myChoices
    String myDialTitle
    String myDialText

    OfbizDialogBuilder(Project project) {
        myProject = project
        myChoices = [:]
    }

    /* codenarc-disable SpaceAroundMapEntryColon */

    OfbizDialogBuilder from(List<String> choicesList) {
        choicesList.forEach { option -> myChoices << [(option): null] }
        return this
    }
    /* codenarc-enable SpaceAroundMapEntryColon */

    OfbizDialogBuilder from(Map<String, Object> choices) {
        myChoices = choices
        return this
    }

    OfbizDialogBuilder title(String title) {
        myDialTitle = title
        return this
    }

    OfbizDialogBuilder text(String text) {
        myDialText = text
        return this
    }

    OfbizSimpleListDialog get() {
        return new OfbizSimpleListDialog(myProject, myChoices,
                myDialTitle ?: OfbizEditorBundle.message('editor.choice.title.default'),
                myDialText ?: OfbizEditorBundle.message('editor.choice.select.default')
        )
    }

}
