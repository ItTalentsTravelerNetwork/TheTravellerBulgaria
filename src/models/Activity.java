package models;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import exceptions.InvalidDataException;
import exceptions.InvalidLocationException;

public class Activity {

	private String name;
	private double price;
	private Location location;
	private String description;
	private CopyOnWriteArrayList<String> pictures;
	private double authorRating;

	public Activity(String name, double price, double lattitude, double longitude, String description,
			CopyOnWriteArrayList<String> pictures, double authorRating)
			throws InvalidDataException, InvalidLocationException {
		super();
		this.setName(name);
		this.setPrice(price);
		this.setLocation(new Location(lattitude, longitude));
		this.setDescription(description);
		this.setPictures(pictures);
		this.setAuthorRating(authorRating);
	}

	public synchronized ArrayList<String> getPictures() {
		ArrayList<String> copy = new ArrayList<>();
		copy.addAll(pictures);
		return copy;
	}

	public synchronized void setPictures(CopyOnWriteArrayList<String> pictures) {
		if (pictures != null) {
			this.pictures = pictures;
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

	public synchronized void addPicture(String picture) {
		if (picture != null && !picture.isEmpty()) {
			this.pictures.add(picture);
		}
	}

	public synchronized void removePicture(String picture) {
		if (picture != null && !picture.isEmpty()) {
			this.pictures.remove(picture);
		}
	}

}
