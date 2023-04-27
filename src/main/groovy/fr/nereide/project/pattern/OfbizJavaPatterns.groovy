package fr.nereide.project.pattern

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

import static com.intellij.patterns.PlatformPatterns.*
import static com.intellij.patterns.PsiJavaElementPattern.Capture
import static fr.nereide.project.pattern.OfbizPatternConst.*

class OfbizJavaPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final PsiElementPattern SERVICE_CALL = psiElement().andOr(
            makeDispatchContextJavaMethodPattern('makeValidContext', 0),
            makeLocalDispatcherJavaMethodPattern('runSync', 0),
            makeLocalDispatcherJavaMethodPattern('runAsync', 0),
            makeLocalDispatcherJavaMethodPattern('runAsyncWait', 0),
            makeLocalDispatcherJavaMethodPattern('runSyncIgnore', 0),
            makeLocalDispatcherJavaMethodPattern('runSyncNewTrans', 0),
            makeLocalDispatcherJavaMethodPattern('schedule', 0),
            makeScriptHelperJavaMethodPattern('runService', 0),
    )

    public static final PsiElementPattern ENTITY_CALL = psiElement().andOr(
            makeDelegatorJavaMethodPattern('find', 0),
            makeDelegatorJavaMethodPattern('findOne', 0),
            makeDelegatorJavaMethodPattern('findAll', 0),
            makeDelegatorJavaMethodPattern('findCountByCondition', 0),
            makeDelegatorJavaMethodPattern('findList', 0),
            makeDelegatorJavaMethodPattern('makeValue', 0),
            makeEntityQueryJavaMethodPattern('from', 0),
            makeDynamicViewEntityJavaMethodPattern('addMemberEntity', 1),
            makeEntityDataServiceJavaMethodPattern('makeGenericValue', 1),
    )

    public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
            makeUtilPropertiesJavaMethodPattern('getMessage', 1)
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_FROM_GV_OBJECT = psiElement().inside(
            psiElement().andOr(
                    makeGenericEntityJavaMethodPattern('get', 0),
                    makeGenericEntityJavaMethodPattern('getString', 0),
                    makeGenericEntityJavaMethodPattern('getBigDecimal', 0),
                    makeGenericEntityJavaMethodPattern('getBoolean', 0),
                    makeGenericEntityJavaMethodPattern('getBytes', 0),
                    makeGenericEntityJavaMethodPattern('getDate', 0),
                    makeGenericEntityJavaMethodPattern('getDouble', 0),
                    makeGenericEntityJavaMethodPattern('getDuration', 0),
                    makeGenericEntityJavaMethodPattern('getFloat', 0),
                    makeGenericEntityJavaMethodPattern('getInteger', 0),
                    makeGenericEntityJavaMethodPattern('getLong', 0)
            )
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_IN_DVE = psiElement().inside(
            psiElement().andOr(
                    makeDynamicViewEntityJavaMethodPattern('addAlias', 1)
            )
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_IN_WHERE_QUERY = psiElement().inside(
            psiElement().andOr(
                    makeEntityQueryJavaMethodPattern('where', 0),
            )
    )

    public static final PsiElementPattern SERVICE_CALL_COMPL = psiElement()
            .inside(SERVICE_CALL)

    public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement()
            .inside(ENTITY_CALL)

    //============================================
    //       UTILITY METHODS
    //============================================
    static Capture<PsiLiteralExpression> makeMethodJavaPattern(String methodName, String className, int index) {
        return makeMethodPattern(PsiJavaPatterns::literalExpression(), methodName, className, index)
    }

    static Capture<PsiLiteralExpression> makeGenericEntityJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, GENERIC_ENTITY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeGenericValueJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, GENERIC_VALUE_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeLocalDispatcherJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, LOCAL_DISPATCHER_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDelegatorJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, DELEGATOR_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDispatchContextJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, DISPATCH_CONTEXT_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeScriptHelperJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, SCRIPT_HELPER_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeEntityQueryJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, ENTITY_QUERY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeDynamicViewEntityJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeEntityDataServiceJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, ENTITY_DATA_SERVICES_CLASS, index)
    }

    static Capture<PsiLiteralExpression> makeUtilPropertiesJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, UTIL_PROPERTIES_CLASS, index)
    }
}
