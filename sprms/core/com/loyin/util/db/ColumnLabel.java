package com.jyd.util.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * <pre>
 * 表字段显示名称
 * 如select COM1 字段一 from table1 
 * 那么对应的POJO是
 * @ Column(name="COM1")
 * @ ColumnLabel(name="字段一")
 * private String com1
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnLabel
{
String name();
}
