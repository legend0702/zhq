package cn.zhuhongqing.util.id;

import java.io.Serializable;

public interface IDGen<T extends Serializable> {

	T nextId();

}
