package com.chenxh.libroompluser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoAlterField {
    int oldVer();
    int newVer();
    AlterTable.Type colType();
    String colName() default "";
    String oldColName() default "";
}
