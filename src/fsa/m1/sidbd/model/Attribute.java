package fsa.m1.sidbd.model;

public class Attribute {
	private String name;
	private String value;


	public Attribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}


	//getters and setters


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}



}
