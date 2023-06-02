package org.apache.ofbiz.idea.plugin.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import org.apache.ofbiz.idea.plugin.reference.common.ServiceReference
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
            if (org.apache.ofbiz.idea.plugin.project.pattern.OfbizXmlPatterns.SERVICE_DEF_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveService(xmlAttrValue) : null
            } else if (org.apache.ofbiz.idea.plugin.project.pattern.OfbizXmlPatterns.ENTITY_OR_VIEW_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveEntityOrView(xmlAttrValue) : null
            }
        }
        /*************
         *    JAVA   *
         *************/
        if (org.apache.ofbiz.idea.plugin.project.pattern.OfbizJavaPatterns.SERVICE_CALL.accepts(contextElement)) {
            return resolveService(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        if (org.apache.ofbiz.idea.plugin.project.pattern.OfbizJavaPatterns.ENTITY_CALL.accepts(contextElement)) {
            return resolveEntityOrView(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        /*************
         *  GROOVY   *
         * useless ? *
         *************/
        return null
    }

    private static PsiElement resolveEntityOrView(XmlAttributeValue attr) {
        org.apache.ofbiz.idea.plugin.reference.common.EntityReference entity = new org.apache.ofbiz.idea.plugin.reference.common.EntityReference(attr)
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
        org.apache.ofbiz.idea.plugin.reference.common.EntityReference entity = new org.apache.ofbiz.idea.plugin.reference.common.EntityReference(expr)
        return entity.resolve()
    }
}
