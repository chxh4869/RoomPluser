package com.chenxh.libroompluser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AnnoAlertTables.class)
public @interface AnnoAlertTable {
    int oldVer();
    int newVer();
    AlterTable.Action action() default AlterTable.Action.Add;
    AlterTable.Type colType();
    String colName();
}
