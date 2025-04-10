package fr.nereide.inspection.common

import com.intellij.codeInspection.LocalInspectionTool

class OfbizBaseInspection extends LocalInspectionTool {

    OfbizBaseInspection() {}

    @Override
    boolean isEnabledByDefault() {
        return true
    }

}
