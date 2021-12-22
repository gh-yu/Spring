package com.kh.ajax.model.vo;

public class Sample {
	private String name;
	private int age;
	
	public Sample() {}
	
	public Sample(String name) {
		super();
		this.name = name;
	}	

	public Sample(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Sample [name=" + name + ", age=" + age + "]";
	}
}
