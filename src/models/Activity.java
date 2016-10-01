package models;

import exceptions.InvalidDataException;
import exceptions.InvalidLocationException;

public class Activity {

	private String name;
	private Location location;
	private String description;
	private String picture;
	private double authorRating;
	private double price;
	private String destinationName;

	public Activity(String name, double price, double lattitude, double longitude, String description, String picture,
			double authorRating) throws InvalidDataException, InvalidLocationException {
		super();
		this.setName(name);
		this.setPrice(price);
		this.setLocation(new Location(lattitude, longitude));
		this.setDescription(description);
		this.setPicture(picture);
		this.setAuthorRating(authorRating);
	}

	public synchronized String getPicture() {
		return this.picture;
	}

	public synchronized void setPicture(String picture) {
		if (picture != null && !picture.isEmpty()) {
			this.picture = picture;
		}
	}

	public synchronized double getAuthorRating() {
		return authorRating;
	}

	public synchronized void setAuthorRating(double authorRating) {
		if (authorRating >= 0) {
			this.authorRating = authorRating;
		}
	}

	public synchronized String getName() {
		return name;
	}

	private synchronized void setName(String name) throws InvalidDataException {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			throw new InvalidDataException();
		}
	}

	public synchronized double getPrice() {
		return price;
	}

	public synchronized void setPrice(double price) {
		if (price >= 0) {
			this.price = price;
		}
	}

	public synchronized Location getLocation() throws CloneNotSupportedException {
		return location;
	}

	public synchronized void setLocation(Location location) throws InvalidLocationException {
		if (location != null) {
			this.location = location;
		} else {
			throw new InvalidLocationException();
		}
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		if (description != null && !description.isEmpty()) {
			this.description = description;
		}
	}

}
