package cn.zhuhongqing.utils.struct;

public interface AcceptFilter<T> {

	boolean accept(T target);

}
