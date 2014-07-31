package cn.zhuhongqing.i18n;

/**
 * 全局消息接口
 * 
 * 给于一个messageKey 获得一条message
 * 
 * 实现该接口必须实现获取消息的方法
 * 
 * @author zhq mail:qwepoidjdj(a)gmail.com
 * @version $Id: Message.java 20 2014-01-07 11:17:36Z legend $
 * @since 1.5
 * 
 */

public interface Message {

	String getMessage(String messageKey);

}
