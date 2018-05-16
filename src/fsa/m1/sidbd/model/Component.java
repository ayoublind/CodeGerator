package fsa.m1.sidbd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Component implements Serializable{

	private static final long serialVersionUID = 1L;

	private String imageUrl;
	private String name;
	private int type;
	private boolean isContainer;
	private List<Component> childrens;
	private Component parentCompo;

	//constructeur
	public Component(String imageUrl, String name, int t, Component parent) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.type = t;
		this.parentCompo = parent;
	}

	//constructeur
	public Component(String imageUrl, String name, int t, Component parent, boolean iscontainer, ArrayList<Component> childrens) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.type = t;
		this.isContainer = iscontainer;
		this.parentCompo = parent;

		if(isContainer){
			this.childrens = childrens;
		}
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

	public boolean isContainer() {
		return isContainer;
	}
	public void setContainer(boolean isContainer) {
		this.isContainer = isContainer;
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

	public List<Component> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Component> childrens) {
		this.childrens = childrens;
	}
}
