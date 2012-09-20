package com.jyd.util.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *<pre>
 *不可使用
 *标识不用来数据库操作字段
 *</pre>
 * @author loyin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Disabled {

}
