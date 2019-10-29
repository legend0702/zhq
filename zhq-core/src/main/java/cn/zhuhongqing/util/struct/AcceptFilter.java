package cn.zhuhongqing.util.struct;

public interface AcceptFilter<T> {

	boolean accept(T target);
	
	public static <T> AcceptFilter<T> allAccpet() {
		return new DefaultAllAcceptFilter<T>();
	}

	class DefaultAllAcceptFilter<T> implements AcceptFilter<T> {

		@Override
		public boolean accept(T target) {
			return true;
		}
	}

}
