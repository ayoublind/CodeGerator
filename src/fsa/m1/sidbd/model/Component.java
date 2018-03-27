package fsa.m1.sidbd.model;

public class Component {
	private String imageUrl;
	private String name;

	//constructeur
	public Component(String imageUrl, String name) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
	}


	//getters and setters
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
