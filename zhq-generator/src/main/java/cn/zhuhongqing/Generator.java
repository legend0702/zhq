package cn.zhuhongqing;

import java.util.Collection;
import java.util.Collections;

import cn.zhuhongqing.generator.GenConfig;
import cn.zhuhongqing.generator.GenProcess;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.module.Module;

/**
 * 生成器<br/>
 * 根据输入/输出的文件(或目录)地址 以及传入的模型参数(model) 进行文件生成<br/>
 * <b>文件(或目录)说明：</b><br/>
 * 
 * <br/>
 * <b>模型说明：</b><br/>
 * 模型是一个k/v的对象(如Map或JavaBean)<br/>
 * 模型里的参数可以用在文件的属性(如名字),最多的还是用在文件内容的生成上.<br/>
 * 文件名称与内容渲染根据模板生成器不同而不同.(模板生成器请见:{@link Template})<br/>
 * <br/>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class Generator extends Module {
	private static GenProcess genProcess = new GenProcess();

	public static String GENERATOR = getModuleName(Generator.class);

	/**
	 * @see #gen(GenConfig, Collection)
	 */

	public static void gen(String tempFileOrRootPath, String outFileOrRootPath, Object model) {
		gen(new GenConfig(tempFileOrRootPath, outFileOrRootPath), model);
	}

	/**
	 * @see #gen(GenConfig, Collection)
	 */

	public static void gen(String tempFileOrRootPath, String outFileOrRootPath, Collection<?> models) {
		gen(new GenConfig(tempFileOrRootPath, outFileOrRootPath), models);
	}

	/**
	 * @see #gen(GenConfig, Collection)
	 */

	public static void gen(FileIOParams tempFileOrRootPath, FileIOParams outFileOrRootPath, Object model) {
		gen(tempFileOrRootPath, outFileOrRootPath, model);
	}

	/**
	 * @see #gen(GenConfig, Collection)
	 */

	public static void gen(FileIOParams tempFileOrRootPath, FileIOParams outFileOrRootPath, Collection<?> models) {
		gen(new GenConfig(tempFileOrRootPath, outFileOrRootPath), models);
	}

	/**
	 * @see #gen(GenConfig, Collection)
	 */

	public static void gen(GenConfig info, Object model) {
		gen(info, Collections.singleton(model));
	}

	/**
	 * 执行生产器
	 */

	public static void gen(GenConfig info, Collection<?> models) {
		genProcess.init(info, models);
		genProcess.execute();
	}

	/**
	 * 创建一个生成器对象
	 */

	public static GenProcess create() {
		return new GenProcess();
	}
}
