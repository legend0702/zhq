package cn.zhuhongqing;

import java.io.Serializable;
import java.util.Date;

public class Person extends People implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idCard;
	public String name;
	private Integer age;
	private Date birthday;
	private Person firend;
	private Serializable ser;

	public static void hello() {

	}

	protected static void bye() {

	}

	void pri() {
		System.out.println("Person pri!");
	}

	@Son
	public void say() {
		System.out.println("Hello World!");
	}

	protected void pro() {
		System.out.println("Person pro!");
	}

	public String getIdCard() {
		return idCard;
	}

	@Son
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Person getFirend() {
		return firend;
	}

	public void setFirend(Person firend) {
		this.firend = firend;
	}

	public Serializable getSer() {
		return ser;
	}

	public <T extends Serializable> void setSer(T ser) {
		this.ser = ser;
	}

}
