package net.loyin.jFinal.anatation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义freemarker标签注解
 * 在target上使用
 * @author 刘声凤
 *  2012-9-4 上午11:48:26
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FtltargetBind {
	/**对应的路径名 已/开头*/
	String name();
}
