package cn.zhuhongqing.utils.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * For class<T> -> T Pairs.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class ClassObjectHole implements Cloneable {

	protected Map<Class<?>, Object> hole;

	public ClassObjectHole() {
		hole = new HashMap<>();
	}

	public ClassObjectHole(Map<Class<?>, Object> hole) {
		this.hole = hole;
	}

	public void put(Object obj) {
		hole.put(obj.getClass(), obj);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clz) {
		return (T) hole.get(clz);
	}

	@Override
	public ClassObjectHole clone() {
		return new ClassObjectHole(new HashMap<>(hole));
	}

}
