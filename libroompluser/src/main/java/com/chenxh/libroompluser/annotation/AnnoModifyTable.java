package com.chenxh.libroompluser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AnnoModifyTables.class)
public @interface AnnoModifyTable {
    int oldVer() default 0;
    int newVer() default 0;
    String sqlExuce();
}
