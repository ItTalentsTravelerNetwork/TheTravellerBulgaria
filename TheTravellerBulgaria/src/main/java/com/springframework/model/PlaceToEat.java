package com.springframework.model;

public class PlaceToEat {
	private String name;
	private Location location;
	private String description;
	private String picture;
	private double authorRating;
	private String destinationName;

	public PlaceToEat(String name, double lattitude, double longitude, String description, String picture,
			double authorRating, String destinationName) {
		super();
		this.destinationName = destinationName;
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setDescription(description);
		setPictures(picture);
		setAuthorRating(authorRating);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPictures(String picture) {
		if (picture != null && !picture.isEmpty())
			this.picture = picture;
	}

	public double getAuthorRating() {
		return authorRating;
	}

	public void setAuthorRating(double authorRating) {
		if (authorRating >= 0)
			this.authorRating = authorRating;
	}

	public void setName(String name) {
		if (name != null && !name.isEmpty())
			this.name = name;
	}

	public void setLocation(Location location) {
		if (location != null)
			this.location = location;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() throws CloneNotSupportedException {
		return location;
	}

	public String getDestinationName() {
		return destinationName;
	}
}
