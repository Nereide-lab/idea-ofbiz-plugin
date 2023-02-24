package fr.nereide.project.pattern

import com.intellij.patterns.PsiJavaElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiLiteral

class OfbizPatternConst {
    public static final String DISPATCH_CONTEXT_CLASS = 'org.apache.ofbiz.service.DispatchContext'
    public static final String LOCAL_DISPATCHER_CLASS = 'org.apache.ofbiz.service.LocalDispatcher'
    public static final String SCRIPT_HELPER_CLASS = 'org.apache.ofbiz.base.util.ScriptHelper'
    public static final String DELEGATOR_CLASS = 'org.apache.ofbiz.entity.Delegator'
    public static final String DYNAMIC_VIEW_ENTITY_CLASS = 'org.apache.ofbiz.entity.model.DynamicViewEntity'
    public static final String ENTITY_DATA_SERVICES_CLASS = 'org.apache.ofbiz.entityext.data.EntityDataServices'
    public static final String ENTITY_QUERY_CLASS = 'org.apache.ofbiz.entity.util.EntityQuery'
    public static final String UTIL_PROPERTIES_CLASS = 'org.apache.ofbiz.base.util.UtilProperties'

    static Object makeMethodPattern(PsiJavaElementPattern<? extends PsiLiteral, ?> elementPattern,
                                    String methodName, String className, int index) {
        return elementPattern.methodCallParameter(index, PsiJavaPatterns.psiMethod().withName(methodName).definedInClass(className))
    }
}
