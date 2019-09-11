package cn.zhuhongqing.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.StringUtils;

public class ProxyTest {

	public static class PlayerProxy implements InvocationHandler {

		private Cache cache = new Cache();
		private Object p;

		public PlayerProxy(Play p) {
			this.p = p;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// 不是set方法不管
			if (!method.getName().startsWith(StringPool.BEAN_SET)) {
				return method.invoke(p, args);
			}
			String fieldName = StringUtils.cutPrefix(method.getName(), StringPool.BEAN_SET);
			String keySign = p.getClass() + fieldName;
			// 一般set参数应该只有一个 如有特殊情况 则需要特殊处理
			Object o = args[0];
			try {
				System.out.println("写缓存啦:" + o.toString());
				cache.set(keySign, o);
			} catch (Exception e) {
				// cache更新失败 直接回滚(不触发方法)
				System.out.println("写缓存失败啦:" + o.toString());
				return null;
				// throw new CacheException("写入缓存失败",e);
			}
			System.out.println("写缓存成功 更新bean:" + o.toString());
			return method.invoke(p, args);
		}

	}

	public static void main(String[] args) {
		Play p = (Play) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class<?>[] { Play.class },
				new PlayerProxy(new Player()));
		p.setName("zhangsan");
		System.out.println(p.getName());

		// List<Play> l1 = Collections.singletonList(p);
		// List<Play> l2 = Collections.singletonList(p);
		//
		// System.out.println("l1:" + l1.get(0).getName());
		// System.out.println("l2:" + l2.get(0).getName());
		//
		// l1.get(0).setName("lisi");
		// System.out.println("l2:" + l2.get(0).getName());

	}

	public static interface Play {

		void setName(String name);

		String getName();

	}

	public static class Player implements Play {

		private String name;

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	public static class Service {

		// @Cache("name")
		public void touch(Play p) {
			p.setName("lisi1");
			p.setName("lisi2");
			// do something
		}

	}

	public static class Cache {

		public void set(String key, Object val) throws Exception {

		}

	}

}
