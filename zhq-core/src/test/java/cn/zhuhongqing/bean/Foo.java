package cn.zhuhongqing.bean;

public class Foo extends Woo {

	private static final long serialVersionUID = 1L;

	private String name;
	private Integer age;
	private Boolean sex;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

}
