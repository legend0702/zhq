package cn.zhuhongqing.anno;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * For annotation value() to key.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

@Repeatable(ValueFors.class)
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface ValueFor {

	/** forName */
	String value();

	Class<?> forClass() default Object.class;

}
