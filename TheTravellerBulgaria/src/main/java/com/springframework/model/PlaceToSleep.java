package com.springframework.model;

public class PlaceToSleep {
	public enum Type {
		HOTEL, ECOHOTEL, HOSTEL, MOTEL, COTTAGE, MANSION, RESORT, VILLA, APARTMENT, CAMP, TENT, FARMHOUSE, GUESTHOUSE
	}

	private String name;
	private Location location;
	private String description;
	private String picture;
	private double authorRating;
	private Type type;
	private String contact;
	private double price;
	private String destinationName;

	public PlaceToSleep(String name, double lattitude, double longitude, String description, String picture,
			double authorRating, Type type, String contact, double price, String destinationName) {
		super();
		setName(name);
		setLocation(new Location(lattitude, longitude));
		setContact(contact);
		setDescription(description);
		setType(type);
		setPrice(price);
		setPicture(picture);
		setAuthorRating(authorRating);
		this.destinationName = destinationName;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}

	public Location getLocation() throws CloneNotSupportedException {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price >= 0)
			this.price = price;
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

	public void setContact(String contact) {
		if (contact != null && !contact.isEmpty())
			this.contact = contact;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		if (picture != null && !picture.isEmpty())
			this.picture = picture;
	}

	public String getDestinationName() {
		return destinationName;
	}

}
