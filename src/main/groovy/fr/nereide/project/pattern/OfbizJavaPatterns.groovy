package fr.nereide.project.pattern

import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiLiteralExpression

import static com.intellij.patterns.PlatformPatterns.psiElement
import static com.intellij.patterns.PsiJavaElementPattern.Capture
import static com.intellij.patterns.PsiJavaPatterns.psiExpression
import static com.intellij.patterns.PsiJavaPatterns.psiMethod
import static fr.nereide.project.pattern.OfbizPatternConst.DELEGATOR_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.DISPATCH_CONTEXT_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.DYNAMIC_VIEW_ENTITY_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_DATA_SERVICES_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.ENTITY_QUERY_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.GENERIC_ENTITY_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.GENERIC_VALUE_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.LOCAL_DISPATCHER_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.MODEL_KEYMAP_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.SCRIPT_HELPER_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.UTIL_PROPERTIES_CLASS
import static fr.nereide.project.pattern.OfbizPatternConst.makeMethodParameterPattern

class OfbizJavaPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final PsiElementPattern SERVICE_CALL = psiElement().andOr(
            makeDispatchContextJavaMethodParameterPattern('makeValidContext', 0),
            makeLocalDispatcherJavaMethodParameterPattern('runSync', 0),
            makeLocalDispatcherJavaMethodParameterPattern('runAsync', 0),
            makeLocalDispatcherJavaMethodParameterPattern('runAsyncWait', 0),
            makeLocalDispatcherJavaMethodParameterPattern('runSyncIgnore', 0),
            makeLocalDispatcherJavaMethodParameterPattern('runSyncNewTrans', 0),
            makeLocalDispatcherJavaMethodParameterPattern('schedule', 0),
            makeScriptHelperJavaMethodParameterPattern('runService', 0),
    )

    public static final PsiElementPattern ENTITY_CALL = psiElement().andOr(
            makeDelegatorJavaMethodParameterPattern('find', 0),
            makeDelegatorJavaMethodParameterPattern('findOne', 0),
            makeDelegatorJavaMethodParameterPattern('findAll', 0),
            makeDelegatorJavaMethodParameterPattern('findCountByCondition', 0),
            makeDelegatorJavaMethodParameterPattern('findList', 0),
            makeDelegatorJavaMethodParameterPattern('makeValue', 0),
            makeEntityQueryJavaMethodParameterPattern('from', 0),
            makeDynamicViewEntityJavaMethodParameterPattern('addMemberEntity', 1),
            makeEntityDataServiceJavaMethodParameterPattern('makeGenericValue', 1),
            makeGenericValueJavaMethodParameterPattern('getRelated', 0)
    )

    public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
            makeUtilPropertiesJavaMethodParameterPattern('getMessage', 1)
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_FROM_GV_OBJECT = psiElement().inside(
            psiElement().andOr(
                    makeGenericEntityJavaMethodParameterPattern('get', 0),
                    makeGenericEntityJavaMethodParameterPattern('getString', 0),
                    makeGenericEntityJavaMethodParameterPattern('getBigDecimal', 0),
                    makeGenericEntityJavaMethodParameterPattern('getBoolean', 0),
                    makeGenericEntityJavaMethodParameterPattern('getBytes', 0),
                    makeGenericEntityJavaMethodParameterPattern('getDate', 0),
                    makeGenericEntityJavaMethodParameterPattern('getDouble', 0),
                    makeGenericEntityJavaMethodParameterPattern('getDuration', 0),
                    makeGenericEntityJavaMethodParameterPattern('getFloat', 0),
                    makeGenericEntityJavaMethodParameterPattern('getInteger', 0),
                    makeGenericEntityJavaMethodParameterPattern('getLong', 0)
            )
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_IN_DVE = psiElement().inside(
            psiElement().andOr(
                    makeDynamicViewEntityJavaMethodParameterPattern('addAlias', 1),
                    makeDynamicViewEntityJavaMethodParameterPattern('addAlias', 2)
            )
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_IN_WHERE_QUERY = psiElement().inside(
            psiElement().andOr(
                    makeEntityQueryJavaMethodParameterPattern('where', 0),
            )
    )

    public static final PsiElementPattern ENTITY_FIELD_INKEYMAP_IN_DVE_0 = psiElement().inside(
            psiElement().andOr(
                    makeModelKeyMapJavaMethodParameterPattern('makeKeyMapList', 0)
                            .inside(psiExpression().methodCall(psiMethod()
                                    .withName('addViewLink')
                                    .definedInClass(DYNAMIC_VIEW_ENTITY_CLASS)))
            )
    )

    public static final PsiElementPattern ENTITY_FIELD_INKEYMAP_IN_DVE_1 = psiElement().inside(
            psiElement().andOr(
                    makeModelKeyMapJavaMethodParameterPattern('makeKeyMapList', 1)
                            .inside(psiExpression().methodCall(psiMethod()
                                    .withName('addViewLink')
                                    .definedInClass(DYNAMIC_VIEW_ENTITY_CLASS)))
            )
    )

    public static final PsiElementPattern SERVICE_CALL_COMPL = psiElement()
            .inside(SERVICE_CALL)

    public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement()
            .inside(ENTITY_CALL)

    //============================================
    //       UTILITY METHODS
    //============================================
    static Capture<PsiLiteralExpression> makeJavaMethodParameterPattern(String methodName, String className, int index) {
        return makeMethodParameterPattern(PsiJavaPatterns::literalExpression(), methodName, className, index)
    }

    static Capture<PsiLiteralExpression> makeGenericEntityJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, GENERIC_ENTITY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeGenericValueJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, GENERIC_VALUE_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeLocalDispatcherJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, LOCAL_DISPATCHER_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDelegatorJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, DELEGATOR_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDispatchContextJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, DISPATCH_CONTEXT_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeScriptHelperJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, SCRIPT_HELPER_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeEntityQueryJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, ENTITY_QUERY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeModelKeyMapJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, MODEL_KEYMAP_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDynamicViewEntityJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeEntityDataServiceJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, ENTITY_DATA_SERVICES_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeUtilPropertiesJavaMethodParameterPattern(String methodName, int index) {
        return makeJavaMethodParameterPattern(methodName, UTIL_PROPERTIES_CLASS, index)
    }
}
