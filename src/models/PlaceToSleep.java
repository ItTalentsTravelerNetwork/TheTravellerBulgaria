package models;

import java.util.concurrent.CopyOnWriteArrayList;

public class PlaceToSleep {
	public enum Type {
		HOTEL, ECOHOTEL, HOSTEL, MOTEL, COTTAGE, MANSION, RESORT, VILLA, APARTMENT, CAMP, TENT, FARMHOUSE, GUESTHOUSE
	}

	private String name;
	private Location location;
	private String contact;
	private String description;
	private Type type;
	private double price;
	private CopyOnWriteArrayList<String> pictures;
	private double authorRating;

	public PlaceToSleep(String name, double lattitude, double longitude, String contact, String description, Type type,
			double price, CopyOnWriteArrayList<String> pictures, double authorRating) {
		super();
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setContact(contact);
		setDescription(description);
		setType(type);
		setPrice(price);
		setPictures(pictures);
		setAuthorRating(authorRating);
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized String getContact() {
		return contact;
	}

	public synchronized Location getLocation() throws CloneNotSupportedException {
		return location;
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public synchronized double getPrice() {
		return price;
	}

	public synchronized void setPrice(double price) {
		if (price >= 0)
			this.price = price;
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

	public synchronized void setContact(String contact) {
		if (contact != null && !contact.isEmpty())
			this.contact = contact;
	}

	public synchronized void setType(Type type) {
		this.type = type;
	}

	public synchronized Type getType() {
		return type;
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
