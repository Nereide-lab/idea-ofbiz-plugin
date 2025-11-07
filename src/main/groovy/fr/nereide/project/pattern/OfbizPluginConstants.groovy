package fr.nereide.project.pattern

import com.intellij.patterns.PsiJavaElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiLiteral

/**
 * String constants
 */
class OfbizPluginConstants {

    // Base const
    public static final String BASE_OFB_PACKAGE = 'org.apache.ofbiz'
    public static final int DEFAULT_COMPLETION_PRIORITY = 100

    // classes
    public static final String DISPATCH_CONTEXT_CLASS = 'org.apache.ofbiz.service.DispatchContext'
    public static final String LOCAL_DISPATCHER_CLASS = 'org.apache.ofbiz.service.LocalDispatcher'
    public static final String SCRIPT_HELPER_CLASS = 'org.apache.ofbiz.base.util.ScriptHelper'
    public static final String DELEGATOR_CLASS = 'org.apache.ofbiz.entity.Delegator'
    public static final String DYNAMIC_VIEW_ENTITY_CLASS = 'org.apache.ofbiz.entity.model.DynamicViewEntity'
    public static final String ENTITY_DATA_SERVICES_CLASS = 'org.apache.ofbiz.entityext.data.EntityDataServices'
    public static final String ENTITY_QUERY_CLASS = 'org.apache.ofbiz.entity.util.EntityQuery'
    public static final String UTIL_PROPERTIES_CLASS = 'org.apache.ofbiz.base.util.UtilProperties'
    public static final String GENERIC_VALUE_CLASS = 'org.apache.ofbiz.entity.GenericValue'
    public static final String GENERIC_ENTITY_CLASS = 'org.apache.ofbiz.entity.GenericEntity'
    public static final String MODEL_KEYMAP_CLASS = 'org.apache.ofbiz.entity.model.ModelKeyMap'

    // Strings
    public static final String QUERY_FROM_STATEMENT = 'from('
    public static final String DYNAMIC_VIEW_ENTITY_CLASS_NAME = 'DynamicViewEntity'
    public static final String DYNAMIC_STRING_DOLLAR = '\\$'
    public static final String DOT_MAP_PROP_ACCESSOR = '.'
    public static final String CLOSING_BRACKET = '}'

    static Object makeMethodParameterPattern(PsiJavaElementPattern<? extends PsiLiteral, ?> elementPattern,
                                             String methodName, String className, int index) {
        return elementPattern.methodCallParameter(index, PsiJavaPatterns.psiMethod()
                .withName(methodName)
                .definedInClass(className))
    }

    public static final String FILE_AND_ELEMENT_SEPARATOR = '#'

}
