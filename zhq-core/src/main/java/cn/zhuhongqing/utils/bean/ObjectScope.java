package cn.zhuhongqing.utils.bean;

/**
 * 类的生命周期<br/>
 * 在类上面，{@value #DEFAULT}跟{@value #SINGLETON}效果是一样的<br/>
 * 在成员变量或者构造函数上时，{@link #DEFAULT}则表示与类声明一致
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public enum ObjectScope {

	DEFAULT, SINGLETON, PROTOTYPE;

}