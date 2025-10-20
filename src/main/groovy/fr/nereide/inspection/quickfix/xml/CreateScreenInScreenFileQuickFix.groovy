package fr.nereide.inspection.quickfix.xml

import com.intellij.psi.xml.XmlFile
import fr.nereide.inspection.InspectionBundle

/**
 * Quick-fix that attempts to create a scree
 */
class CreateScreenInScreenFileQuickFix extends BaseOfbizElementCreateQuickFix {

    final String tagType = 'screen'

    CreateScreenInScreenFileQuickFix(XmlFile file, String screenName) {
        super(file, screenName)
    }

    @Override
    String getName() {
        return InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
    }

}
