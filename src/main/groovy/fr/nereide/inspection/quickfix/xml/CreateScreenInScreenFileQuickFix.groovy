package fr.nereide.inspection.quickfix.xml

import com.intellij.psi.xml.XmlFile
import fr.nereide.inspection.InspectionBundle

class CreateScreenInScreenFileQuickFix extends BaseOfbizElementCreateQuickFix {

    CreateScreenInScreenFileQuickFix(XmlFile file, String screenName) {
        super(file, screenName)
    }

    @Override
    String getName() {
        return InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
    }

    @Override
    String getTagType() {
        return 'screen'
    }
}
