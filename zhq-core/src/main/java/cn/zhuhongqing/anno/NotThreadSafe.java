package cn.zhuhongqing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被该注解装饰的类都是非线程安全的<br/>
 * 当多线程环境下使用这些类时需要注意线程安全问题,以免出现错误<br/>
 * 并且线程安全问题有一定的传染性,当一个类依赖一个非线程安全的对象,那么这个类极有可能也是非线程安全的<br/>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface NotThreadSafe {

}
