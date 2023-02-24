package fr.nereide.project.pattern

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiLiteralExpression

class OfbizJavaPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final PsiElementPattern SERVICE_CALL = PlatformPatterns.psiElement().andOr(
            makeDispatchContextJavaMethodPattern("makeValidContext", 0),
            makeLocalDispatcherJavaMethodPattern('runSync', 0),
            makeLocalDispatcherJavaMethodPattern('runAsync', 0),
            makeLocalDispatcherJavaMethodPattern('runAsyncWait', 0),
            makeLocalDispatcherJavaMethodPattern('runSyncIgnore', 0),
            makeLocalDispatcherJavaMethodPattern('runSyncNewTrans', 0),
            makeLocalDispatcherJavaMethodPattern('schedule', 0),
            makeScriptHelperJavaMethodPattern("runService", 0),
    )

    public static final PsiElementPattern ENTITY_CALL = PlatformPatterns.psiElement().andOr(
            makeDelegatorJavaMethodPattern('find', 0),
            makeDelegatorJavaMethodPattern('findOne', 0),
            makeDelegatorJavaMethodPattern('findAll', 0),
            makeDelegatorJavaMethodPattern('findCountByCondition', 0),
            makeDelegatorJavaMethodPattern('findList', 0),
            makeDelegatorJavaMethodPattern('makeValue', 0),
            makeEntityQueryJavaMethodPattern('from', 0),
            makeDynamicViewEntityJavaMethodPattern('addMemberEntity', 1),
            makeEntityDataServiceJavaMethodPattern("makeGenericValue", 1),
    )

    public static final PsiElementPattern LABEL_CALL = PlatformPatterns.psiElement().andOr(
            makeUtilPropertiesJavaMethodPattern("getMessage", 1)
    )

    public static final PsiElementPattern SERVICE_CALL_COMPL = PlatformPatterns.psiElement()
            .inside(SERVICE_CALL)

    public static final PsiElementPattern ENTITY_CALL_COMPL = PlatformPatterns.psiElement()
            .inside(ENTITY_CALL)

    //============================================
    //       UTILITY METHODS
    //============================================
    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeMethodJavaPattern(String methodName, String className, int index) {
        return OfbizPatternConst.makeMethodPattern(PsiJavaPatterns::literalExpression(), methodName, className, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeLocalDispatcherJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.LOCAL_DISPATCHER_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeDelegatorJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.DELEGATOR_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeDispatchContextJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.DISPATCH_CONTEXT_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeScriptHelperJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.SCRIPT_HELPER_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeEntityQueryJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.ENTITY_QUERY_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeDynamicViewEntityJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeEntityDataServiceJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.ENTITY_DATA_SERVICES_CLASS, index)
    }

    static PsiJavaElementPattern.Capture<PsiLiteralExpression> makeUtilPropertiesJavaMethodPattern(String methodName, int index) {
        return makeMethodJavaPattern(methodName, OfbizPatternConst.UTIL_PROPERTIES_CLASS, index)
    }
}
