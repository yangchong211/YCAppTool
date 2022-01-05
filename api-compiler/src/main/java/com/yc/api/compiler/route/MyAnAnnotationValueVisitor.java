package com.yc.api.compiler.route;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

public class MyAnAnnotationValueVisitor extends SimpleAnnotationValueVisitor8<String, Void> {
    @Override
    public String visitType(TypeMirror typeMirror, Void o) {
        return typeMirror.toString();
    }
}
