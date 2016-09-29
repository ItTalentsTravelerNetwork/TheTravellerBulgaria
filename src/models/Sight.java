package models;

import java.util.concurrent.CopyOnWriteArrayList;

import exceptions.InvalidDataException;
import exceptions.InvalidLocationException;

public class Sight {
	private String name;
	private Location location;
	private String description;
	private CopyOnWriteArrayList<String> pictures;
	private double authorRating;

	public Sight(String name, double lattitude, double longitude, String description,
			CopyOnWriteArrayList<String> pictures, double authorRating)
			throws InvalidDataException, InvalidLocationException {
		super();
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setDescription(description);
		setPictures(pictures);
		setAuthorRating(authorRating);
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) throws InvalidDataException {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			throw new InvalidDataException();
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

	public synchronized CopyOnWriteArrayList<String> getPictures() {
		return pictures;
	}

	public synchronized void setPictures(CopyOnWriteArrayList<String> pictures) {
		if (pictures != null && !pictures.isEmpty())
			this.pictures = pictures;
	}

	public synchronized double getAuthorRating() {
		return authorRating;
	}

	public synchronized void setAuthorRating(double authorRating) {
		if (authorRating >= 0)
			this.authorRating = authorRating;
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
