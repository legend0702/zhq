package cn.zhuhongqing;

import java.io.Serializable;

/**
 * All Animal
 * 
 * @author HongQing.Zhu
 * 
 */

public interface Animal extends Serializable {
	@Parent
	void say();

	void run();

}
