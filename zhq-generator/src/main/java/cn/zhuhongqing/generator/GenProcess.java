package cn.zhuhongqing.generator;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.Template;
import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.io.FileUtil;
import cn.zhuhongqing.utils.CollectionUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StreamUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.scan.FileInAndExcludeResourceFilter;
import cn.zhuhongqing.utils.scan.ResourceScan;
import cn.zhuhongqing.utils.scan.ResourceScanManager;

/**
 * Gen Process.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

@NotThreadSafe
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

	/**
	 * 主要思路
	 */

	public void execute() {
		checkParam();
		Set<File> tempFiles = fs.getResources(StringUtil.endPadSlashAndAllPattern(config.getTempFileOrRootPath()),
				new FileInAndExcludeResourceFilter(config.getIncludePatterns(), config.getExcludePatterns()));

		if (tempFiles.isEmpty()) {
			throw new GeneratorException(
					"Empty Template-Files found under the File-Path:" + config.getTempFileOrRootPath());
		}

		if (config.isDelete()) {
			FileUtil.cleanDir(config.getOutFileParams().getFile());
		}

		LOG.info("Found " + tempFiles.size() + "Template-Files,now is start generate Render-File.");

		for (Object model : getModels()) {
			boolean discard = false;
			for (GeneratorFilter filter : config.getFilters()) {
				model = filter.beforeAll(model);
				if (GeneralUtil.isNull(model)) {
					discard = true;
					break;
				}
			}
			if (discard) {
				continue;
			}
			gen0(tempFiles, config, model);
		}

		LOG.info("Gen is finished.The Out-File path is:" + config.getOutFileOrRootPath());
	}

	private void gen0(Set<File> temps, GenConfig config, Object model) {
		for (File temp : temps) {
			Object currentModel = model;
			FileIOParams inParams = new FileIOParams(temp.getAbsolutePath(), config.getTempFileParams().getCharset());
			boolean discard = false;
			for (GeneratorFilter filter : config.getFilters()) {
				currentModel = filter.beforeGen(currentModel, inParams);
				if (GeneralUtil.isNull(currentModel)) {
					discard = true;
					break;
				}
			}
			if (discard) {
				continue;
			}
			InputStreamReader isr = inParams.toInStreamReader();
			OutputStreamWriter osw = createOutFileIoParams(temp, currentModel).toOutStreamWriter();
			try {
				template.render(isr, osw, currentModel);
			} finally {
				StreamUtil.close(isr);
				StreamUtil.close(osw);
			}

		}
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