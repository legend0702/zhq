package cn.zhuhongqing.utils.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.bean.ObjectScope;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER })
public @interface SPI {
	/** GROUP SIGN */
	String value() default StringPool.DEFAULT;

	/** Life Scope */
	ObjectScope scope() default ObjectScope.DEFAULT;

}