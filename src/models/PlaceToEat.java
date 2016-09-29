package models;

import java.util.concurrent.CopyOnWriteArrayList;

public class PlaceToEat {
	private String name;
	private Location location;
	private String description;
	private CopyOnWriteArrayList<String> pictures;
	private double authorRating;

	public PlaceToEat(String name, double lattitude, double longitude, String description,
			CopyOnWriteArrayList<String> pictures, double authorRating) {
		super();
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setDescription(description);
		setPictures(pictures);
		setAuthorRating(authorRating);
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public synchronized CopyOnWriteArrayList<String> getPictures() {
		return pictures;
	}

	public synchronized void setPictures(CopyOnWriteArrayList<String> pictures) {
		if (pictures != null)
			this.pictures = pictures;
	}

	public synchronized double getAuthorRating() {
		return authorRating;
	}

	public synchronized void setAuthorRating(double authorRating) {
		if (authorRating >= 0)
			this.authorRating = authorRating;
	}

	public synchronized void setName(String name) {
		if (name != null && !name.isEmpty())
			this.name = name;
	}

	public synchronized void setLocation(Location location) {
		if (location != null)
			this.location = location;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized Location getLocation() throws CloneNotSupportedException {
		return location;
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
