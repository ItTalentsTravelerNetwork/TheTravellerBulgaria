package models;

import exceptions.InvalidDataException;
import exceptions.InvalidLocationException;

public class Sight {
	private String name;
	private Location location;
	private String description;
	private String picture;
	private double authorRating;
	private String destinationName;

	public Sight(String name, double lattitude, double longitude, String description, String picture,
			double authorRating, String destinationName) throws InvalidDataException, InvalidLocationException {
		super();
		this.destinationName = destinationName;
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setDescription(description);
		setPicture(picture);
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

	public synchronized String getPicture() {
		return picture;
	}

	public synchronized void setPicture(String picture) {
		if (picture != null && !picture.isEmpty())
			this.picture = picture;
	}

	public synchronized double getAuthorRating() {
		return authorRating;
	}

	public synchronized void setAuthorRating(double authorRating) {
		if (authorRating >= 0)
			this.authorRating = authorRating;
	}

	public String getDestinationName() {
		return destinationName;
	}

}
