package cn.zhuhongqing.dbmeta;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import cn.zhuhongqing.dbmeta.oracle.Oracle11DBMetaInfo;
import cn.zhuhongqing.dbmeta.postgresql.Postgresql9MetaInfo;
import cn.zhuhongqing.util.GeneralUtils;

/**
 * 用于存放{@link DBMetaInfo}的具体子类实现
 * 
 * 并提供注册、获取以及删除的方法获取具体实现
 * 
 * 由于{@link DBMetaInfo}本身不是线程安全的 因此为了保证在多线程下不会出错 每次获取的对象都是新new的
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class DBMetaPool {

	private static HashMap<String, ConcurrentSkipListMap<Double, DBMetaInfo>> META_POOL = new HashMap<String, ConcurrentSkipListMap<Double, DBMetaInfo>>();

	static {
		DBMetaPool.register(new Postgresql9MetaInfo());
		DBMetaPool.register(new Oracle11DBMetaInfo());
	}

	private DBMetaPool() {

	}

	/**
	 * 增加一个新的DBMetaInfo实例
	 */

	public static final void register(DBMetaInfo metaInfo) {
		ConcurrentSkipListMap<Double, DBMetaInfo> dataBaseMap = META_POOL.get(metaInfo.getDataBaseName());
		if (GeneralUtils.isNull(dataBaseMap)) {
			dataBaseMap = new ConcurrentSkipListMap<Double, DBMetaInfo>();
			META_POOL.put(metaInfo.getDataBaseName().toLowerCase(), dataBaseMap);
		}
		dataBaseMap.put(metaInfo.getVersion(), metaInfo);
	}

	public static final DBMetaInfo unregister(String dataBaseName, Double version) {
		ConcurrentSkipListMap<Double, DBMetaInfo> dataBaseMap = META_POOL.get(dataBaseName.toLowerCase());
		if (GeneralUtils.isNull(dataBaseMap)) {
			return null;
		}
		return dataBaseMap.remove(version);
	}

	public static final DBMetaInfo get(String dataBaseName, Double version) {
		ConcurrentSkipListMap<Double, DBMetaInfo> dataBaseMap = META_POOL.get(dataBaseName.toLowerCase());
		if (GeneralUtils.isNull(dataBaseMap)) {
			// throw new DBMetaException("没有找到链接为[" + url +
			// "]的DBMeta实现类,要不你来试着做一个?");
			return new DBMetaInfoDef();
		}
		DBMetaInfo info = dataBaseMap.get(version);
		if (GeneralUtils.isNotNull(info)) {
			return info.clone();
		}
		/**
		 * 尝试获取高于给定版本的实例
		 * 
		 * 如果存在 那么返回最高版本的实例
		 */
		Entry<Double, DBMetaInfo> entry = dataBaseMap.higherEntry(version);
		if (GeneralUtils.isNotNull(entry)) {
			return dataBaseMap.lastEntry().getValue().clone();
		}
		// 没有就返回最接近的
		return dataBaseMap.lowerEntry(version).getValue().clone();
	}

}
