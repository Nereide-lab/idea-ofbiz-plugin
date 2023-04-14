package fr.nereide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.project.pattern.OfbizJavaPatterns
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.common.EntityReference
import fr.nereide.reference.common.ServiceReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class OfbizElementResolverForDocumentationProvider extends AbstractDocumentationProvider {

    private static final Logger LOG = Logger.getInstance(OfbizElementResolverForDocumentationProvider.class)

    @Override
    PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement, int targetOffset) {
        // TODO : rajouter les autres types possibles
        PsiElement xmlAttr = PsiTreeUtil.getParentOfType(contextElement, XmlAttribute.class)
        /*************
         *    XML    *
         *************/
        if (xmlAttr) {
            PsiElement xmlAttrValue = PsiTreeUtil.getChildOfType(xmlAttr, XmlAttributeValue)
            if (OfbizXmlPatterns.SERVICE_DEF_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveService(xmlAttrValue) : null
            } else if (OfbizXmlPatterns.ENTITY_OR_VIEW_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveEntityOrView(xmlAttrValue) : null
            }
        }
        /*************
         *    JAVA   *
         *************/
        if (OfbizJavaPatterns.SERVICE_CALL.accepts(contextElement)) {
            return resolveService(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        if (OfbizJavaPatterns.ENTITY_CALL.accepts(contextElement)) {
            return resolveEntityOrView(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        /*************
         *  GROOVY   *
         * useless ? *
         *************/
        return null
    }

    private static PsiElement resolveEntityOrView(XmlAttributeValue attr) {
        EntityReference entity = new EntityReference(attr)
        return entity.resolve()
    }

    private static PsiElement resolveService(XmlAttributeValue attr) {
        ServiceReference service = new ServiceReference(attr)
        return service.resolve()
    }

    private static PsiElement resolveService(PsiLiteralExpression expr) {
        ServiceReference service = new ServiceReference(expr)
        return service.resolve()
    }

    private static PsiElement resolveEntityOrView(PsiLiteralExpression expr) {
        EntityReference entity = new EntityReference(expr)
        return entity.resolve()
    }
}
