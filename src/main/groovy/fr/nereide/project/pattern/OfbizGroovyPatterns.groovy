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
    public static final PsiElementPattern SERVICE_CALL = psiElement().andOr(
            makeLocalDispatcherGroovyMethodPattern('runSync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsync', 0),
            makeLocalDispatcherGroovyMethodPattern('runAsyncWait', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncIgnore', 0),
            makeLocalDispatcherGroovyMethodPattern('runSyncNewTrans', 0),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('runService')),
            //TODO : bétonner un peu pour éviter les soucis de référence non trouvée ?
            groovyLiteralExpression().withParent(namedArgument().withLabel('service')
            )
    )

    public static final PsiElementPattern ENTITY_CALL = psiElement().andOr(
            makeDelegatorGroovyMethodPattern('find', 0),
            makeDelegatorGroovyMethodPattern('findOne', 0),
            makeDelegatorGroovyMethodPattern('findAll', 0),
            makeDelegatorGroovyMethodPattern('findCountByCondition', 0),
            makeDelegatorGroovyMethodPattern('findList', 0),
            makeDynamicViewEntityGroovyMethodPattern('addMemberEntity', 1),
            makeEntityDataServicesGroovyMethodPattern('makeGenericValue', 1),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('from')),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('makeValue')),
            groovyLiteralExpression().methodCallParameter(0, psiMethod().withName('findOne'))
    )

    public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
            makeUtilPropertiesGroovyMethodPattern('getMessage', 1)
    )

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE = psiElement().andOr(
            psiElement().afterLeafSkipping(
                    psiElement().withText('.'),
                    psiElement().withParent(psiElement().with(new FieldTypeCondition(
                            'GenericValueTypePattern',
                            'org.apache.ofbiz.entity.GenericValue', 'GenericValue'))
                    )
            )
    )

    public static final PsiElementPattern SERVICE_CALL_COMPL = psiElement().inside(SERVICE_CALL)

    public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement().inside(ENTITY_CALL)

    public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE_COMPL = psiElement().inside(GENERIC_VALUE_ATTRIBUTE)

    public static final PsiElementPattern GROOVY_LOOP_PATTERN = psiElement().andOr(
            psiElement().withText(string().contains('forEach')),
            psiElement().withText(string().contains('stream'))
    )

    //============================================
    //       UTILITY METHODS
    //============================================
    static Capture<GrLiteral> makeGroovyMethodPattern(String methodName, String className, int index) {
        return makeMethodPattern(GroovyPatterns::groovyLiteralExpression(), methodName, className, index)
    }

    static Capture<GrLiteral> makeLocalDispatcherGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, LOCAL_DISPATCHER_CLASS, index)
    }

    static Capture<GrLiteral> makeDelegatorGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, DELEGATOR_CLASS, index)
    }

    static Capture<GrLiteral> makeDynamicViewEntityGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, DYNAMIC_VIEW_ENTITY_CLASS, index)
    }

    static Capture<GrLiteral> makeEntityDataServicesGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, ENTITY_DATA_SERVICES_CLASS, index)
    }

    static Capture<GrLiteral> makeUtilPropertiesGroovyMethodPattern(String methodName, int index) {
        return makeGroovyMethodPattern(methodName, UTIL_PROPERTIES_CLASS, index)
    }
}
