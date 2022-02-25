package com.yc.logging.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * 保持class级别混淆
 */
@Retention(CLASS)
@Target({PACKAGE,TYPE,ANNOTATION_TYPE,CONSTRUCTOR,METHOD,FIELD})
public @interface KeepClass {
}
