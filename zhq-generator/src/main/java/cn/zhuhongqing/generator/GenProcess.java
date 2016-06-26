package cn.zhuhongqing.generator;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.Template;
import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.io.FileUtil;
import cn.zhuhongqing.io.StreamUtil;
import cn.zhuhongqing.utils.BeanWrap;
import cn.zhuhongqing.utils.CollectionUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.scan.FileInAndExcludeResourceFilter;
import cn.zhuhongqing.utils.scan.ResourceScan;
import cn.zhuhongqing.utils.scan.ResourceScanManager;

/**
 * 模板渲染执行器<br/>
 * 
 * 主要流程:<br/>
 * 
 * <pre>
 * 1.检查参数
 * 2.扫描模板文件并检查
 * 3.循环模型参数并执行过滤器部分->循环模板文件并执行过滤器部分->用模型渲染模板生成文件
 * </pre>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class GenProcess {

	private static final Logger LOG = LoggerFactory.getLogger(GenProcess.class);

	private GenConfig config;
	private Collection<?> models;
	private Template template;
	private ResourceScan<File> fs = ResourceScanManager.getResourceScan(File.class);

	public GenProcess() {
		template = Template.createRender();
	}

	public GenProcess(GenConfig config, Collection<?> models) {
		this();
		init(config, models);
	}

	public void init(GenConfig config, Collection<?> models) {
		this.config = config;
		this.models = models;
	}

	public GenConfig getconfig() {
		return config;
	}

	public void setConfig(GenConfig config) {
		this.config = config;
	}

	public Collection<?> getModels() {
		return models;
	}

	public void setModels(Collection<?> models) {
		this.models = models;
	}

	public void execute() {
		checkParam();
		Set<File> tempFiles = getTempFiles();

		if (tempFiles.isEmpty()) {
			throw new GeneratorException(
					"Empty Template-Files found under the File-Path:" + config.getTempFileOrRootPath());
		}

		if (config.isDelete()) {
			FileUtil.cleanDir(config.getOutFileParams().getFile());
		}

		LOG.info("Found " + tempFiles.size() + "Template-Files,now is start generate Render-File.");

		for (Object model : getModels()) {
			execute0(model, tempFiles);
		}

		LOG.info("Gen is finished.The Out-File path is:" + config.getOutFileOrRootPath());
	}

	private void execute0(Object model, Set<File> tempFiles) {
		BeanWrap beanWrap = new BeanWrap(model);
		for (GeneratorFilter filter : config.getFilters()) {
			if (!filter.beforeAll(beanWrap)) {
				LOG.info("The model [ " + beanWrap.merge() + "] is abandoned by [ " + filter + " ] filter.");
				return;
			}
		}
		gen0(tempFiles, beanWrap);
	}

	private void gen0(Set<File> temps, BeanWrap mainWrap) {
		for (File temp : temps) {
			gen1(temp, mainWrap);
		}
	}

	private void gen1(File temp, BeanWrap mainWrap) {
		BeanWrap cloneWrap = mainWrap.clone();
		FileIOParams inParams = new FileIOParams(temp.getAbsolutePath(), config.getTempFileParams().getCharset());
		for (GeneratorFilter filter : config.getFilters()) {
			if (!filter.beforeGen(cloneWrap, config.getTempFileParams(), inParams)) {
				LOG.info("The tempFile [ " + temp + "] on model [ " + cloneWrap.merge() + " ] is abandoned by [ "
						+ filter + " ] filter.");
				return;
			}
		}
		Object model = cloneWrap.merge();
		InputStreamReader isr = inParams.toInStreamReader();
		OutputStreamWriter osw = createOutFileIoParams(temp, model).toOutStreamWriter();
		try {
			template.render(isr, osw, model);
		} finally {
			StreamUtil.close(isr);
			StreamUtil.close(osw);
		}

	}

	private Set<File> getTempFiles() {
		return fs.getResources(StringUtil.endPadSlashAndAllPattern(config.getTempFileOrRootPath()),
				new FileInAndExcludeResourceFilter(config.getIncludePatterns(), config.getExcludePatterns()));
	}

	private FileIOParams createOutFileIoParams(File temp, Object model) {
		String uriPath = FileUtil.toURISchemeSpecificPart(temp);
		String suffix = StringUtil.cutToLastIndexOf(uriPath, StringPool.DOT);
		String $subPath = StringUtil.cutStartFromEnd(uriPath,
				FileUtil.toURISchemeSpecificPart(config.getTempFileParams().getFile()), suffix);
		String subPath = template.render($subPath, model);
		String outFilePath = config.getOutFileOrRootPath() + StringPool.SLASH + subPath + suffix;
		FileIOParams outFileParams = new FileIOParams(outFilePath, config.getOutFileParams().getCharset());
		FileUtil.mkdirs(outFileParams.getFile().getParentFile());
		return outFileParams;
	}

	private void checkParam() {
		if (StringUtil.isEmpty(config.getTempFileOrRootPath())) {
			throw new GeneratorException("Template-File path must not an empty string.");
		}

		File tempFile = config.getTempFileParams().getFile();
		if (!FileUtil.isExisAndReadable(tempFile)) {
			throw new GeneratorException("Template-File path must a exists and readable file or directory.");
		}

		if (StringUtil.isEmpty(config.getOutFileOrRootPath())) {
			throw new GeneratorException("Out-File path must not an empty string.");
		}

		File outFile = config.getOutFileParams().getFile();
		if (outFile.exists()) {
			if (outFile.isFile()) {
				throw new GeneratorException("Out-File path must a exists directory.");
			}
		} else {
			FileUtil.mkdirs(outFile);
		}

		if (FileUtil.equals(tempFile, outFile)) {
			throw new GeneratorException("Template-File path must different from the Out-File path.");
		}

		if (CollectionUtil.isEmpty(models)) {
			throw new GeneratorException("Models must not an empty Collection.");
		}

		if (GeneralUtil.isNull(template)) {
			throw new GeneratorException("Template create failed.");
		}
	}

}