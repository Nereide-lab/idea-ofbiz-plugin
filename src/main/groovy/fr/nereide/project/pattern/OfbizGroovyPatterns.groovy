package fr.nereide.project.pattern

import com.intellij.patterns.PsiElementPattern
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns

import static com.intellij.patterns.PlatformPatterns.psiElement
import static com.intellij.patterns.PsiJavaPatterns.psiMethod
import static com.intellij.patterns.StandardPatterns.string
import static fr.nereide.project.pattern.OfbizPatternConst.*
import static org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyElementPattern.Capture
import static org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns.groovyLiteralExpression
import static org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns.namedArgument

class OfbizGroovyPatterns {

    //============================================
    //       PATTERNS
    //============================================
    public static final PsiElementPattern SERVICE_CALL = psiElement().inside(psiElement().andOr(
            makeLocalDispatcherGroovyMethodPattern('runSync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsyncWait', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncIgnore', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncNewTrans', 0),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('runService')),
            //TODO : bétonner un peu pour éviter les soucis de référence non trouvée ?
            groovyLiteralExpression().withParent(namedArgument().withLabel('service')
            )
    ))

    public static final PsiElementPattern ENTITY_CALL = psiElement().inside(psiElement().andOr(
            makeDelegatorGroovyMethodParameterPattern('find', 0),
            makeDelegatorGroovyMethodParameterPattern('findOne', 0),
            makeDelegatorGroovyMethodParameterPattern('findAll', 0),
            makeDelegatorGroovyMethodParameterPattern('findCountByCondition', 0),
            makeDelegatorGroovyMethodParameterPattern('findList', 0),
            makeDynamicViewEntityGroovyMethodParameterPattern('addMemberEntity', 1),
            makeEntityDataServicesGroovyParameterPattern('makeGenericValue', 1),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('from')),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('makeValue')),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('findOne')),
            makeGenericValueGroovyMethodParameterPattern('getRelated', 0),
    ))

    public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
            makeUtilPropertiesGroovyMethodPattern('getMessage', 1)
    )

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE = psiElement().inside(psiElement().andOr(
            psiElement().afterLeafSkipping(
                    psiElement().withText('.'),
                    psiElement().withParent(psiElement().with(new FieldTypeCondition(
                            'GenericValueTypePattern',
                            'org.apache.ofbiz.entity.GenericValue', 'GenericValue'))
                    )
            )
    ))

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE_GET = psiElement().inside(psiElement().andOr(
            makeGenericEntityGroovyMethodParameterPattern('get', 0),
            makeGenericEntityGroovyMethodParameterPattern('getString', 0),
            makeGenericEntityGroovyMethodParameterPattern('getBigDecimal', 0),
            makeGenericEntityGroovyMethodParameterPattern('getBoolean', 0),
            makeGenericEntityGroovyMethodParameterPattern('getBytes', 0),
            makeGenericEntityGroovyMethodParameterPattern('getDate', 0),
            makeGenericEntityGroovyMethodParameterPattern('getDouble', 0),
            makeGenericEntityGroovyMethodParameterPattern('getDuration', 0),
            makeGenericEntityGroovyMethodParameterPattern('getFloat', 0),
            makeGenericEntityGroovyMethodParameterPattern('getInteger', 0),
            makeGenericEntityGroovyMethodParameterPattern('getLong', 0)
    ))

    public static final PsiElementPattern GENERIC_VALUE_FIELD_IN_DVE = psiElement().inside(
            psiElement().andOr(
                    makeDynamicViewEntityGroovyMethodParameterPattern('addAlias', 1),
                    makeDynamicViewEntityGroovyMethodParameterPattern('addAlias', 2)
            )
    )

    public static final PsiElementPattern GENERIC_VALUE_FIELD_QUERY = psiElement().inside(
            psiElement().andOr(
                    makeEntityQueryGroovyMethodParamaterPattern('where', 0)
            )
    )

    public static final PsiElementPattern ENTITY_FIELD_IN_KEYMAP_IN_DVE_0 = psiElement().inside(
            psiElement().andOr(
                    makeModelKeyMapGroovyMethodParamaterPattern('makeKeyMapList', 0)
                            .inside(GroovyPatterns.methodCall().withMethodName('addViewLink'))
            )
    )

    public static final PsiElementPattern ENTITY_FIELD_IN_KEYMAP_IN_DVE_1 = psiElement().inside(
            psiElement().andOr(
                    makeModelKeyMapGroovyMethodParamaterPattern('makeKeyMapList', 1)
                            .inside(GroovyPatterns.methodCall().withMethodName('addViewLink'))
            )
    )

    public static final PsiElementPattern GROOVY_LOOP_PATTERN = psiElement().andOr(
            psiElement().withText(string().contains('forEach')),
            psiElement().withText(string().contains('stream'))
    )

    //============================================
    //       UTILITY METHODS
    //============================================
    static Capture<GrLiteral> makeGroovyMethodParameterPattern(String methodName, String className, int index) {
        return makeMethodParameterPattern(GroovyPatterns::groovyLiteralExpression(), methodName, className, index)
    }

    static Capture<GrLiteral> makeModelKeyMapGroovyMethodParamaterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, MODEL_KEYMAP_CLASS, index)
    }

    static Capture<GrLiteral> makeEntityQueryGroovyMethodParamaterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, ENTITY_QUERY_CLASS, index)
    }

    static Capture<GrLiteral> makeLocalDispatcherGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, LOCAL_DISPATCHER_CLASS, index)
    }

    static Capture<GrLiteral> makeDelegatorGroovyMethodParameterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, DELEGATOR_CLASS, index)
    }

    static Capture<GrLiteral> makeDynamicViewEntityGroovyMethodParameterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static Capture<GrLiteral> makeEntityDataServicesGroovyParameterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, ENTITY_DATA_SERVICES_CLASS, index)
    }

    static Capture<GrLiteral> makeGenericValueGroovyMethodParameterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, GENERIC_VALUE_CLASS, index)
    }

    static Capture<GrLiteral> makeUtilPropertiesGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, UTIL_PROPERTIES_CLASS, index)
    }

    static Capture<GrLiteral> makeGenericEntityGroovyMethodParameterPattern(String methodName, int index) {
        return makeGroovyMethodParameterPattern(methodName, GENERIC_ENTITY_CLASS, index)
    }
}
