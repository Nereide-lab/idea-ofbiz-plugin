package fr.nereide.inspection.quickfix.xml

import com.intellij.psi.xml.XmlFile
import fr.nereide.inspection.InspectionBundle

/**
 * Quick-fix that attempts to create a file after a user dialog
 */
class CreateFormInFormFileQuickFix extends BaseOfbizElementCreateQuickFix {

    final String name = InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')

    final String tagType = 'form'

    CreateFormInFormFileQuickFix(XmlFile file, String formName) {
        super(file, formName)
    }

}
