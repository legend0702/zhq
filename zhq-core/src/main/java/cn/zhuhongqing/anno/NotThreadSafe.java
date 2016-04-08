package cn.zhuhongqing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被该注解注解的类都不是线程安全的<br/>
 * 当多线程环境下,使用这些类需要注意线程安全问题<br/>
 * 
 * @author HongQing.Zhu
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface NotThreadSafe {

}
