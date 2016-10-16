package com.springframework.model;

import com.springframework.exceptions.InvalidDataException;
import com.springframework.exceptions.InvalidLocationException;

public class Activity {

	private String name;
	private Location location;
	private String description;
	private String picture;
	private double authorRating;
	private double price;
	private String destinationName;

	public Activity(String name, double lattitude, double longitude, String description, String picture,
			double authorRating, double price, String destinationName)
			throws InvalidDataException, InvalidLocationException {
		super();
		this.setName(name);
		this.setPrice(price);
		this.setLocation(new Location(lattitude, longitude));
		this.setDescription(description);
		this.setPicture(picture);
		this.setAuthorRating(authorRating);
		this.destinationName = destinationName;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		if (picture != null && !picture.isEmpty()) {
			this.picture = picture;
		}
	}

	public double getAuthorRating() {
		return authorRating;
	}

	public void setAuthorRating(double authorRating) {
		if (authorRating >= 0) {
			this.authorRating = authorRating;
		}
	}

	public String getName() {
		return name;
	}

	private void setName(String name) throws InvalidDataException {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			throw new InvalidDataException();
		}
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price >= 0) {
			this.price = price;
		}
	}

	public Location getLocation() throws CloneNotSupportedException {
		return location;
	}

	public void setLocation(Location location) throws InvalidLocationException {
		if (location != null) {
			this.location = location;
		} else {
			throw new InvalidLocationException();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null && !description.isEmpty()) {
			this.description = description;
		}
	}

	public String getDestinationName() {
		return destinationName;
	}

}
