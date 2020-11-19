package cn.zhuhongqing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Map.key alias.
 * <pre>
 * @KeyAlias("n_a_m_e")
 * String name = "zhangsan";
 * ==>
 * {"n_a_m_e" : "zhangsan"}
 * <pre>
 * 
 * @author ZHQ
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface KeyAlias {

	String value();

}
