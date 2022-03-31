package com.yc.api.compiler.route;

import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public final class ElementTool {

    public static RouteContract<ClassName> getApiClassNameContract(
            Elements elements, MyAnAnnotationValueVisitor annotationValueVisitor, TypeElement apiImplElement) {
        String apiClassSymbol = null;
        //获取
        List<? extends AnnotationMirror> annotationMirrors = apiImplElement.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> entries = elementValues.entrySet();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : entries) {
                ExecutableElement key = entry.getKey();
                //注意断点打印：com.zwwl.moduleinterface.IShowDialogManager.class
                AnnotationValue value = entry.getValue();
                apiClassSymbol = value.accept(annotationValueVisitor, null);
            }
        }
        TypeElement typeElement = elements.getTypeElement(apiClassSymbol);
        ClassName apiName = ClassName.get(typeElement);
        ClassName apiImplName = ClassName.get(apiImplElement);
        return new RouteContract<>(apiName, apiImplName);
    }


}
