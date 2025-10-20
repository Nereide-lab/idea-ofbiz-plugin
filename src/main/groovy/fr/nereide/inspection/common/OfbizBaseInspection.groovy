package fr.nereide.inspection.common

import com.intellij.codeInspection.LocalInspectionTool

/**
 * Base inspection for Ofbiz plugin
 */
class OfbizBaseInspection extends LocalInspectionTool {

    @Override
    boolean isEnabledByDefault() {
        return true
    }

}
