package cn.zhuhongqing.exception;

import cn.zhuhongqing.module.Module;

/**
 * One module only have one extends-line.
 * 
 * @author HongQing.Zhu
 * 
 */

public class MultiOnceModuleException extends ModuleException {

	private static final long serialVersionUID = 1L;

	private static final String MSG = " can not have subClass more than one.";

	public MultiOnceModuleException(Module module) {
		super("[" + module.getModuleType().getName() + "]" + MSG);
	}

}
