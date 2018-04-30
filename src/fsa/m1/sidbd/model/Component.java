package fsa.m1.sidbd.model;

import java.io.Serializable;

public class Component implements Serializable{

	private static final long serialVersionUID = 1L;

	private String imageUrl;
	private String name;
	private int type;

	private Component parentCompo;

	//constructeur
	public Component(String imageUrl, String name, int t, Component parent) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.type = t;
		this.parentCompo = parent;
	}

	@Override
	public String toString() {
		return name;
	}



	//getters and setters
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Component getParentCompo() {
		return parentCompo;
	}

	public void setParentCompo(Component parent) {
		this.parentCompo = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
