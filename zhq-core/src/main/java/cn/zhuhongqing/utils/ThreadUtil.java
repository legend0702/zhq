package cn.zhuhongqing.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Some utilities for Thread.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new UtilsException(e);
		}
	}

	public static Thread getCurrentThread() {
		return Thread.currentThread();
	}

	public static String getCurrentThreadName() {
		return getCurrentThread().getName();
	}

	public static ExecutorService newSingle() {
		return Executors.newSingleThreadExecutor(callingClassNameFactory());
	}

	public static ExecutorService newSingle(String name) {
		return Executors.newSingleThreadExecutor(nameFactory(name));
	}

	public static ExecutorService newFixed(int number) {
		return Executors.newFixedThreadPool(number, callingClassNameFactory());
	}

	public static ExecutorService newFixed(int number, String name) {
		return Executors.newFixedThreadPool(number, nameFactory(name));
	}

	public static ThreadFactory callingClassNameFactory() {
		return nameFactory(ClassUtil.getCallingClassOut().getName());
	}

	public static ThreadFactory nameFactory(String name) {
		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		};
	}

}
