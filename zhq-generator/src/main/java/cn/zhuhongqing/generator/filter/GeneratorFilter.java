package cn.zhuhongqing.generator.filter;

import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.utils.BeanWrap;

/**
 * 用来在生成前 对Model(模型)进行一些调整 以便更方便的在模板里操作
 */

public interface GeneratorFilter {

	/**
	 * 在模型参数准备渲染文件前 该方法会被调用<br/>
	 * 
	 * 对于每个模型参数 每个Filter的该方法只会执行一次<br/>
	 * 
	 * 该方法早于{@link #beforeGen(BeanWrap, FileIOParams)}进行执行<br/>
	 * 
	 * @param modelWrap
	 *            如果存在多个Filter,该参数会顺序传递<br/>
	 *            当针对该模型参数调用{@link #beforeGen(BeanWrap, FileIOParams)}时,该参数会以BeanWrap参数传递进去
	 * 
	 * @return 如果返回false,那么针对本次模板的渲染会被抛弃.
	 * 
	 */

	public default boolean beforeAll(BeanWrap modelWrap) {
		return true;
	}

	/**
	 * 在每个模板文件进行渲染前 该方法会被调用<br/>
	 * 
	 * 对于每个模板文件 每个Filter的该方法只会执行一次<br/>
	 * 
	 * @param modelWrap
	 *            如果存在多个Filter,该参数会顺序传递.但是对于不同的模板文件,该参数不会产生传递.
	 * 
	 * @param currentInParams
	 *            当前准备渲染的模板文件信息
	 * 
	 * @return 如果返回false,那么针对本次模板文件的渲染会被抛弃.
	 */

	public default boolean beforeGen(BeanWrap modelWrap, FileIOParams currentInParams) {
		return true;
	}

}
