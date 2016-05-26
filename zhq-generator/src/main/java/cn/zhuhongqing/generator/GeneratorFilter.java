package cn.zhuhongqing.generator;

import cn.zhuhongqing.io.FileIOParams;

/**
 * 用来在生成前 对Model(模型)进行一些调整 以便更方便的在模板里操作
 * 
 */

public interface GeneratorFilter {

	/**
	 * 在第一次使用模型参数前进行执行 对于每个模型参数 一个Filter只会执行一次
	 * 
	 * 该方法早于{@link #beforeGen(Object, FileIOParams)}进行执行
	 * 
	 * 如果有多个Filter,那么前一个Filter的返回值将会是下一个Filter的入参,并用最后一个Filter的返回值作为后续的模型参数
	 * 
	 * 如果返回Null 那么关于该model的渲染会被抛弃
	 */

	public default Object beforeAll(Object model) {
		return model;
	}

	/**
	 * 在每个模板文件渲染阿前都会调用该方法
	 * 
	 * 由于渲染中不会对Model进行类似“复制”的操作 因此请谨慎对原model进行变更
	 * 
	 * 如果有多个Filter,那么前一个Filter的返回值将会是下一个Filter的入参,并用最后一个Filter的返回值作为渲染的模型参数
	 * 
	 * 如果返回Null 那么针对本次模板的渲染会被抛弃
	 */

	public default Object beforeGen(Object model, FileIOParams currentInParams) {
		return model;
	}

}
