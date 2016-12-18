package cn.zhuhongqing.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock utils.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public final class LockUtil {

	private static final ConcurrentHashMap<String, Lock> LOCK_HOLE = new ConcurrentHashMap<>();

	/**
	 * @see Lock#tryLock()
	 */

	public static boolean tryLock(String sign) {
		String signIntern = sign.intern();
		Lock lock = LOCK_HOLE.get(signIntern);
		if (GeneralUtil.isNull(lock)) {
			LOCK_HOLE.putIfAbsent(signIntern, new ReentrantLock());
			lock = LOCK_HOLE.get(signIntern);
		}
		return lock.tryLock();
	}

	/**
	 * @see Lock#unlock()
	 */

	public static void unlock(String sign) {
		String signIntern = sign.intern();
		Lock lock = LOCK_HOLE.get(signIntern);
		if (GeneralUtil.isNotNull(lock))
			lock.unlock();
	}

}
