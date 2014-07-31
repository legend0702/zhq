package cn.zhuhongqing;

/**
 * All People commond
 * 
 * @author HongQing.Zhu
 * 
 */

public abstract class People implements Animal {

	private static final long serialVersionUID = 1L;

	private Float height;

	private Float weight;

	public int name;

	void secret() {
		System.out.println("People secret!");
	}

	protected void superPro() {
		System.out.println("People superPro!");
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	@Parent
	public void say(String word) {
		System.out.println("Hello World!");
	}

	public void run() {
		System.out.println("I am running!!!");
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

}
