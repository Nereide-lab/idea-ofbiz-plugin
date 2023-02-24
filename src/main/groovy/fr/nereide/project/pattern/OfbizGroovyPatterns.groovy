package fr.nereide.project.pattern

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.StandardPatterns
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyElementPattern
import org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns

class OfbizGroovyPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final PsiElementPattern SERVICE_CALL = PlatformPatterns.psiElement().andOr(
            makeLocalDispatcherGroovyMethodPattern('runSync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsyncWait', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncIgnore', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncNewTrans', 0),
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod().withName("runService")),
            //TODO : bétonner un peu pour éviter les soucis de référence non trouvée ?
            GroovyPatterns.groovyLiteralExpression().withParent(GroovyPatterns.namedArgument().withLabel('service')
            )
    )

    public static final PsiElementPattern ENTITY_CALL = PlatformPatterns.psiElement().andOr(
            makeDelegatorGroovyMethodPattern("find", 0),
            makeDelegatorGroovyMethodPattern("findOne", 0),
            makeDelegatorGroovyMethodPattern("findAll", 0),
            makeDelegatorGroovyMethodPattern("findCountByCondition", 0),
            makeDelegatorGroovyMethodPattern("findList", 0),
            makeDynamicViewEntityGroovyMethodPattern('addMemberEntity', 1),
            makeEntityDataServicesGroovyMethodPattern('makeGenericValue', 1),
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod().withName("from")),
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod().withName("makeValue")),
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod().withName("findOne"))
    )

    public static final PsiElementPattern LABEL_CALL = PlatformPatterns.psiElement().andOr(
            makeUtilPropertiesGroovyMethodPattern('getMessage', 1)
    )

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().afterLeafSkipping(
                    PlatformPatterns.psiElement().withText("."),
                    PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement().with(new FieldTypeCondition(
                            "GenericValueTypePattern",
                            'org.apache.ofbiz.entity.GenericValue', 'GenericValue'))
                    )
            )
    )

    public static final PsiElementPattern SERVICE_CALL_COMPL = PlatformPatterns.psiElement().inside(SERVICE_CALL)

    public static final PsiElementPattern ENTITY_CALL_COMPL = PlatformPatterns.psiElement().inside(ENTITY_CALL)

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE_COMPL = PlatformPatterns.psiElement().inside(GENERIC_VALUE_ATTRIBUTE)

    public static final PsiElementPattern GROOVY_LOOP_PATTERN = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().withText(StandardPatterns.string().contains('forEach')),
            PlatformPatterns.psiElement().withText(StandardPatterns.string().contains('stream'))
    )

    //============================================
    //       UTILITY METHODS
    //============================================
    static GroovyElementPattern.Capture<GrLiteral> makeGroovyMethodPattern(String methodName, String className, int index) {
        return OfbizPatternConst.makeMethodPattern(GroovyPatterns::groovyLiteralExpression(), methodName, className, index)
    }

    static GroovyElementPattern.Capture<GrLiteral> makeLocalDispatcherGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, OfbizPatternConst.LOCAL_DISPATCHER_CLASS, index)
    }

    static GroovyElementPattern.Capture<GrLiteral> makeDelegatorGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, OfbizPatternConst.DELEGATOR_CLASS, index)
    }

    static GroovyElementPattern.Capture<GrLiteral> makeDynamicViewEntityGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, OfbizPatternConst.DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static GroovyElementPattern.Capture<GrLiteral> makeEntityDataServicesGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, OfbizPatternConst.ENTITY_DATA_SERVICES_CLASS, index)
    }

    static GroovyElementPattern.Capture<GrLiteral> makeUtilPropertiesGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, OfbizPatternConst.UTIL_PROPERTIES_CLASS, index)
    }
}
