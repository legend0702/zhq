package cn.zhuhongqing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.module.Module;
import cn.zhuhongqing.template.TemplateProcess;
import cn.zhuhongqing.template.exception.TemplateException;
import cn.zhuhongqing.utils.SPIUtil;

/**
 * 模板工具包<br/>
 * 如需并行渲染 请用{@link Template#createRender()}创建多个渲染器 并调用非静态方法进行操作
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

@NotThreadSafe
public class Template extends Module {

	private static TemplateProcess STATIC_TP = SPIUtil.load(TemplateProcess.class);

	private TemplateProcess tp;

	protected Template() {
		tp = SPIUtil.load(TemplateProcess.class);
	}

	/**
	 * 解析模板源并结合数据模型进行渲染<br/>
	 * 最后将渲染结果输入到输出源中<br/>
	 * 
	 * @param reader
	 *            模板源
	 * @param writer
	 *            输出源
	 * @param model
	 *            数据模型
	 * @return
	 */

	public static Writer globalRender(Reader reader, Writer writer, Object model) {
		try {
			STATIC_TP.render(reader, writer, model);
			return writer;
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * @param tempPath
	 *            模板文件地址
	 * @param outPath
	 *            渲染文件存放地址
	 * @param model
	 *            数据模型
	 * @return
	 * @throws FileNotFoundException
	 * @see #render(Reader, Writer, Object)
	 */

	public static Writer globalRender(String tempPath, String outPath, Object model) {
		return globalRender(new FileIOParams(tempPath), new FileIOParams(outPath), model);
	}

	/**
	 * 
	 * @param temp
	 *            模板文件地址以及解码参数
	 * @param out
	 *            渲染文件存放地址以及编码参数
	 * @param model
	 *            数据模型
	 * @return
	 * 
	 * @throws FileNotFoundException
	 * @see #render(Reader, Writer, Object)
	 */

	public static Writer globalRender(FileIOParams temp, FileIOParams out, Object model) {
		try {
			return globalRender(new InputStreamReader(new FileInputStream(temp.getPath()), temp.getCharset()),
					new OutputStreamWriter(new FileOutputStream(out.getPath()), out.getCharset()), model);
		} catch (FileNotFoundException e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * 渲染一段文本并返回渲染后的结果
	 */

	public static String globalRender(String strTemp, Object model) {
		StringWriter sw = new StringWriter();
		globalRender(new StringReader(strTemp), sw, model);
		return sw.toString();
	}

	/**
	 * 解析模板源并结合数据模型进行渲染<br/>
	 * 最后将渲染结果输入到输出源中<br/>
	 * 
	 * @param reader
	 *            模板源
	 * @param writer
	 *            输出源
	 * @param model
	 *            数据模型
	 * @return
	 */

	public Writer render(Reader reader, Writer writer, Object model) {
		try {
			tp.render(reader, writer, model);
			return writer;
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * @param tempPath
	 *            模板文件地址
	 * @param outPath
	 *            渲染文件存放地址
	 * @param model
	 *            数据模型
	 * @return
	 * @throws FileNotFoundException
	 * @see #render(Reader, Writer, Object)
	 */

	public Writer render(String tempPath, String outPath, Object model) {
		return render(new FileIOParams(tempPath), new FileIOParams(outPath), model);
	}

	/**
	 * 
	 * @param temp
	 *            模板文件地址以及解码参数
	 * @param out
	 *            渲染文件存放地址以及编码参数
	 * @param model
	 *            数据模型
	 * @return
	 * 
	 * @throws FileNotFoundException
	 * @see #render(Reader, Writer, Object)
	 */

	public Writer render(FileIOParams temp, FileIOParams out, Object model) {
		return render(temp.toInStreamReader(), out.toOutStreamWriter(), model);
	}

	/**
	 * 渲染一段文本并返回渲染后的结果
	 */

	public String render(String strTemp, Object model) {
		StringWriter sw = new StringWriter();
		render(new StringReader(strTemp), sw, model);
		return sw.toString();
	}

	/**
	 * 创建一个新的模型渲染器
	 */

	public static Template createRender() {
		return new Template();
	}
}
