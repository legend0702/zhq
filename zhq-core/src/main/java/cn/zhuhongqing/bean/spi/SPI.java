package cn.zhuhongqing.bean.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.zhuhongqing.bean.ObjectScope;
import cn.zhuhongqing.bean.ObjectState;
import cn.zhuhongqing.util.StringPool;

/**
 * @see ObjectState
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface SPI {
	/** Group Sign */
	String value() default StringPool.DEFAULT;

	/** Life Scope */
	ObjectScope scope() default ObjectScope.DEFAULT;

}