package fr.nereide.inspection.quickfix.xml

import com.intellij.psi.xml.XmlFile
import fr.nereide.inspection.InspectionBundle

class CreateFormInFormFileQuickFix extends BaseOfbizElementCreateQuickFix {

    CreateFormInFormFileQuickFix(XmlFile file, String formName) {
        super(file, formName)
    }

    @Override
    String getName() {
        return InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')
    }

    @Override
    String getTagType() {
        return 'form'
    }
}
