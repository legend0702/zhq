package cn.zhuhongqing.utils.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.LockUtil;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.bean.AbstractBeanFactory;
import cn.zhuhongqing.utils.bean.BeanDefinition;
import cn.zhuhongqing.utils.bean.BeanDefinitionGroup;
import cn.zhuhongqing.utils.bean.BeanInvokable;
import cn.zhuhongqing.utils.bean.BeanProperty;

@SuppressWarnings("rawtypes")
public abstract class SPIAbstractBeanFactory extends AbstractBeanFactory {

	private static final Set<String> LOADED_DIR = Collections.synchronizedSet(new HashSet<>());

	// ================================

	private List<String> roots;

	SPIAbstractBeanFactory(String root) {
		this.roots = StringUtil.split(root);
		load();
	}

	SPIAbstractBeanFactory(String[] roots) {
		this.roots = Arrays.asList(roots);
		load();
	}

	SPIAbstractBeanFactory(List<String> roots) {
		this.roots = roots;
		load();
	}

	private void load() {
		for (String root : this.roots) {
			if (LOADED_DIR.contains(root))
				continue;
			if (LockUtil.tryLock(root)) {
				try {
					addBeanDefinitionGroup(loadClass(root));
					LOADED_DIR.add(root);
				} finally {
					LockUtil.unlock(root);
				}
			}
		}
	}

	/**
	 * 初始化的时候需要加载需要管理的{@link Class}
	 */

	protected abstract Collection<Class> loadClass(String root);

	/**
	 * 将{@link Class}进行注册
	 */

	protected void addBeanDefinitionGroup(Collection<Class> classes) {
		List<Class<?>> noPureClass = new ArrayList<>(classes.size());
		List<Class<?>> pureClass = new ArrayList<>(classes.size());
		for (Class<?> clazz : classes) {
			if (clazz.isInterface() || ClassUtil.isAbstract(clazz)) {
				noPureClass.add(clazz);
			} else {
				pureClass.add(clazz);
			}
		}
		for (Class<?> clazz : noPureClass) {
			register(clazz);
		}
		for (Class<?> clazz : pureClass) {
			register(clazz);
		}
	}

	/**
	 * 将需要管理的{@link Class}注册进来
	 * 
	 * <pre>
	 * 主要说明：
	 * 所有类都有自己的{@link BeanDefinitionGroup},接口或抽象类没有自己的{@link BeanDefinition}
	 * 用{@link SPI#value()}作为group标识 当一个接口出现多个实现类时 可以用该标识区分
	 * </pre>
	 */

	@Override
	public BeanDefinitionGroup register(Class<?> clazz, String group) {
		boolean isPureClass = isPureClass(clazz);
		BeanDefinitionGroup defineGroup = addBeanDefinitionGroup(clazz);
		if (isPureClass) {
			BeanDefinition define = SPIUtil.classToBeanDefinition(clazz);
			if (StringUtil.isNotEmpty(group)) {
				define.setGroup(group);
			}
			if (checkAndAddDefineToGroup(defineGroup, define)) {
				return defineGroup;
			}
			// TODO 可以考虑多构造函数的支持
			SPIUtil.initBeanDefinition(define);
			registerToSupported(define);
		}
		return defineGroup;
	}

	@Override
	protected Object createBeanWithOutProperty(BeanDefinition define) {
		Object obj = null;
		BeanInvokable invoker = SPIUtil.getConstructor(define);
		if (invoker.hasParameter()) {
			Collection<BeanProperty> paramProps = invoker.attributeValues();
			Object[] params = new Object[paramProps.size()];
			int index = 0;
			for (BeanProperty prop : paramProps) {
				params[index] = getBeanByProperty(prop);
				index++;
			}
			obj = invoker.invoke(null, params);
		} else {
			obj = invoker.invoke(null);
		}
		return obj;
	}

	/**
	 * @return 如果是普通的类，返回true;如果是接口或者抽象类，返回false;如果都不是则抛出异常
	 */

	protected boolean isPureClass(Class<?> clazz) {
		if (clazz.isInterface() || ClassUtil.isAbstract(clazz)) {
			return false;
		}
		if (ClassUtil.isPureClass(clazz)) {
			return true;
			// if (ClassUtil.hasInterface(clazz) ||
			// ClassUtil.hasSuperClass(clazz)) {
			// return true;
			// }
			// throw new IllegalArgumentException(
			// "The class type [" + clazz + "] must has interface or
			// SuperClass(not Object.class).");
		}
		throw new IllegalArgumentException("The class type like [" + clazz + "] are not supported.");
	}

	@Override
	protected String getAnnotationGroup(Class<?> clazz) {
		return SPIUtil.getGroup(clazz);
	}

	protected ClassLoader findClassLoader() {
		return ClassUtil.getDefaultClassLoader();
	}
}