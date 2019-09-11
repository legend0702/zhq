package cn.zhuhongqing.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.bean.spi.SPIUtil;
import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.StringUtils;

public abstract class AbstractBeanFactory implements BeanFactory {

	protected static final String SERVICES_DIRECTORY = "META-INF/zhq-bean/*";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String DEF_GROUP = StringPool.DEFAULT;

	private static final ConcurrentMap<Class<?>, BeanDefinitionGroup> BEAN_DEFINITION_GROUP = new ConcurrentHashMap<>();
	private static final ConcurrentMap<BeanDefinition, Object> SINGLETON_INSTANCES = new ConcurrentHashMap<>();

	@Override
	public <T> T getBean(Class<T> clazz, String group) {
		BeanDefinitionGroup defineGroup = checkAndGetDefGroup(clazz);
		String group0 = getGroup(clazz, group);
		BeanDefinition define = defineGroup.getAttr(group0);
		if (GeneralUtils.isNull(define)) {
			Collection<BeanDefinition> defineArr = defineGroup.attrValues();
			// 如果就一个就返回该实例吧
			if (defineArr.size() == 1) {
				define = defineArr.iterator().next();
				logger.warn("Only one class [" + define.getMetaType() + "] for [" + clazz
						+ "],but their group are not same.");
			} else {
				throw new IllegalStateException("The class [ " + defineGroup.getMetaType()
						+ " ] has more than one implement class with group " + defineArr);
			}
		}
		// TODO 如果考虑属性重写 这里就要改
		Object bean = getSingletonInstance(define);
		if (GeneralUtils.isNotNull(bean))
			return clazz.cast(bean);
		bean = createBeanWithOutProperty(define);
		if (define.isSingleton()) {
			bean = putSingletonInstance(define, bean);
		}
		injectBeanProperty(bean, define);
		return clazz.cast(bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getBeans(Class<T> clazz) {
		BeanDefinitionGroup defineGroup = checkAndGetDefGroup(clazz);
		List<T> instances = new ArrayList<>();
		for (BeanDefinition beanDef : defineGroup.attrValues()) {
			instances.add((T) getBean(beanDef.getMetaType(), beanDef.getGroup()));
		}
		return instances;
	}

	private BeanDefinitionGroup checkAndGetDefGroup(Class<?> clazz) {
		BeanDefinitionGroup defineGroup = getBeanDefinitionGroup(clazz);
		if (GeneralUtils.isNull(defineGroup) || defineGroup.isEmptyAttr()) {
			BeanUtils.throwNoBeanFind(clazz);
		}
		return defineGroup;
	}

	private String getGroup(Class<?> clazz, String groupParam) {
		if (GeneralUtils.isNull(groupParam)) {
			groupParam = getGroup(clazz);
		}
		return groupParam;
	}

	@Override
	public Integer getRegisterCount() {
		return BEAN_DEFINITION_GROUP.size();
	}

	@Override
	public Class<?>[] getRegisterClasses() {
		return BEAN_DEFINITION_GROUP.keySet().toArray(ArraysUtils.emptyArray(Class.class));
	}

	@Override
	public void inject(Object bean) {
		BeanDefinition define = getBeanDefinition(bean.getClass());
		if (GeneralUtils.isNull(define)) {
			register(bean.getClass());
			define = getBeanDefinition(bean.getClass());
		}
		injectBeanProperty(bean, define);
	}

	protected Object getBeanForProperty(BeanProperty property) {
		return getBean(property.getMetaType(), property.getGroup());
	}

	/**
	 * 将{@link BeanDefinition}与{@link BeanDefinitionGroup}关联起来
	 * 
	 * 不包含自己的Group，主要是接口以及抽象类
	 */

	protected void registerToSupported(BeanDefinition define) {
		Class<?> ownerClass = define.getMetaType();
		Collection<Class<?>> supported = new ArrayList<>();
		supported.addAll(ReflectUtils.getSuperClasses(ownerClass));
		supported.addAll(ClassUtils.getAllInterfacesForClassAsSet(ownerClass));
		for (Class<?> clazz : supported) {
			BeanDefinitionGroup defineGroup = register(clazz);
			checkAndAddDefineToGroup(defineGroup, define);
		}
	}

	/**
	 * 将define注册到defineGroup中去
	 * 
	 * @param defineGroup
	 *            需要加入的group
	 * @param define
	 *            需要加入的define
	 * @return true:表示是原有值;false:表示是新值
	 */

	protected boolean checkAndAddDefineToGroup(BeanDefinitionGroup defineGroup, BeanDefinition define) {
		String group0 = define.getGroup();
		String metaGroup = getAnnotationGroup(defineGroup.getMetaType());
		// 声明上不带标记的 并且是默认名字的 则按不怎么会冲突的方式处理
		if (StringUtils.isEmpty(metaGroup)
				&& (define.isDefaultScope() || ClassUtils.isObjectClass(defineGroup.getMetaType()))) {
			group0 = BeanUtils.getClassNameForGroup(define);
		}
		BeanDefinition defineOld = defineGroup.getAttr(group0);
		if (GeneralUtils.isNotNull(defineOld)) {
			// 如果是自己或者子类就不管了
			if (defineOld.getMetaType().isAssignableFrom(define.getMetaType())) {
				return true;
			}
			/* 1.针对拥有标记需要管理的数据 必须保证他们不会冲突 2.类名冲突也要改 */
			BeanUtils.throwDupGroup(defineGroup.getMetaType(), group0, defineOld.getMetaType(), define.getMetaType());
			return false;// 不会到这里
		} else {
			// 如果是自己 则直接按标准存放
			if (defineGroup.getMetaType().equals(define.getMetaType())) {
				defineGroup.setAttr(define.getGroup(), define);
				return false;
			}
			defineGroup.setAttr(group0, define);
			return false;
		}
	}

	protected void injectBeanProperty(Object bean, BeanDefinition define) {
		Collection<BeanProperty> fieldProps = define.attrValues();
		if (fieldProps.isEmpty())
			return;
		for (BeanProperty fieldProp : fieldProps) {
			BeanProperty prop = fieldProp;
			if (!prop.isField())
				continue;
			Object obj = getBeanForProperty(prop);
			BeanUtils.setProperty(bean, prop.getName(), obj);
		}
	}

	protected BeanDefinitionGroup addBeanDefinitionGroup(Class<?> clazz) {
		return putBeanDefinitionGroup(clazz, BeanDefinitionGroup.build(clazz));
	}

	protected BeanDefinition getBeanDefinition(Class<?> clazz) {
		BeanDefinitionGroup defineGroup = getBeanDefinitionGroup(clazz);
		if (GeneralUtils.isNull(defineGroup))
			return null;
		return defineGroup.getAttr(getGroup(clazz));
	}

	protected BeanDefinitionGroup getBeanDefinitionGroup(Class<?> clazz) {
		return BEAN_DEFINITION_GROUP.get(clazz);
	}

	protected BeanDefinitionGroup putBeanDefinitionGroup(Class<?> clazz, BeanDefinitionGroup defineGroup) {
		BeanDefinitionGroup old = BEAN_DEFINITION_GROUP.putIfAbsent(clazz, defineGroup);
		return GeneralUtils.defValue(old, defineGroup);
	}

	protected Object putSingletonInstance(BeanDefinition define, Object instance) {
		Object old = SINGLETON_INSTANCES.putIfAbsent(define, instance);
		return GeneralUtils.defValue(old, instance);
	}

	protected Object getSingletonInstance(BeanDefinition define) {
		return SINGLETON_INSTANCES.get(define);
	}

	protected String getGroup(Class<?> clazz) {
		return GeneralUtils.defValue(getAnnotationGroup(clazz), DEF_GROUP);
	}

	protected String getAnnotationGroup(Class<?> clazz) {
		return SPIUtil.getGroup(clazz);
	}

	// abstract

	protected abstract Object createBeanWithOutProperty(BeanDefinition define);

}
