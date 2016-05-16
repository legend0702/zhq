package cn.zhuhongqing.generator;

import cn.zhuhongqing.io.FileIOParams;

/**
 * 模板生成器参数
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class GenConfig {

	/**
	 * 模板文件或模板的根目录地址
	 */
	private FileIOParams tempFileOrRootPath;

	/**
	 * 输出文件或是文件目录的地址
	 */
	private FileIOParams outFileOrRootPath;

	/**
	 * 是否清理生成目标文件或是文件目录<br/>
	 * 主要用于当生成文件时 如果生成文件的地址或文件目录已经存在 那么会将该文件或是文件目录进行清理操作 避免出现新旧文件混淆<br/>
	 * 如果关闭 那么当出现重名文件时 旧文件会被新生成的文件覆盖<br/>
	 * 默认为true<br/>
	 */
	private boolean isDelete = true;

	/**
	 * 在扫描模板文件时 以文件名称匹配的方式 扫描文件纳入模板列表<br/>
	 * "/"符号结尾表示匹配目录<br/>
	 * 支持通配符:<br/>
	 * 单字匹配=?<br/>
	 * 多字匹配=*<br/>
	 */
	private String[] includePatterns = new String[0];
	/**
	 * 在匹配成功的基础上 以文件名称匹配的方式 移除不需要的模板文件 <br/>
	 * "/"符号结尾表示匹配目录<br/>
	 * 支持通配符:<br/>
	 * 单字匹配=?<br/>
	 * 多字匹配=*<br/>
	 */
	private String[] excludePatterns = new String[0];

	public GenConfig(String tempFileOrRootPath, String outFileOrRootPath) {
		this.tempFileOrRootPath = new FileIOParams(tempFileOrRootPath);
		this.outFileOrRootPath = new FileIOParams(outFileOrRootPath);
	}

	public GenConfig(FileIOParams tempFileOrRootPath, FileIOParams outFileOrRootPath) {
		this.tempFileOrRootPath = tempFileOrRootPath;
		this.outFileOrRootPath = outFileOrRootPath;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public FileIOParams getTempFileParams() {
		return tempFileOrRootPath;
	}

	public FileIOParams getOutFileParams() {
		return outFileOrRootPath;
	}

	public String getTempFileOrRootPath() {
		return tempFileOrRootPath.getFile().getAbsolutePath();
	}

	public void setTempFileOrRootPath(String tempFileOrRootPath) {
		this.tempFileOrRootPath = new FileIOParams(tempFileOrRootPath);
	}

	public String getOutFileOrRootPath() {
		return outFileOrRootPath.getFile().getAbsolutePath();
	}

	public void setOutFileOrRootPath(String outFileOrRootPath) {
		this.outFileOrRootPath = new FileIOParams(outFileOrRootPath);
	}

	public void setTempFileOrRootPath(FileIOParams tempFileOrRootPath) {
		this.tempFileOrRootPath = tempFileOrRootPath;
	}

	public void setOutFileOrRootPath(FileIOParams outFileOrRootPath) {
		this.outFileOrRootPath = outFileOrRootPath;
	}

	public String[] getIncludePatterns() {
		return includePatterns;
	}

	public void setIncludePatterns(String... includePatterns) {
		this.includePatterns = includePatterns;
	}

	public String[] getExcludePatterns() {
		return excludePatterns;
	}

	public void setExcludePatterns(String... excludePatterns) {
		this.excludePatterns = excludePatterns;
	}

}
