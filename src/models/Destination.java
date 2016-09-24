package models;

import java.util.ArrayList;

import functionality.UsersManager;

public class Destination {
	private String name;
	private String description;
	private ArrayList<Comment> comments;
	private ArrayList<PlaceToSleep> placesToSleep;
	private ArrayList<PlaceToEat> placesToEat;
	private Location location;
	private String picture;
	private String authorEmail;

	public Destination(String name, String description, Location location, String picture, String authorEmail) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.picture = picture;
		this.authorEmail = authorEmail;
		this.comments = new ArrayList<>();
		this.placesToSleep = new ArrayList<>();
		this.placesToEat = new ArrayList<>();
	}

	public void addPlaceToSleep(PlaceToSleep place) {
		synchronized (this) {
			if (place != null)
				this.placesToSleep.add(place);
		}
	}

	public void addComment(Comment comment) {
		synchronized (this) {
			if (comment != null)
				this.comments.add(comment);
		}
	}

	public void addPlaceToEat(PlaceToEat place) {
		synchronized (this) {
			if (place != null)
				this.placesToEat.add(place);
		}
	}

	public String getName() {
		synchronized (this) {
			return name;
		}
	}

	public String getDescription() {
		synchronized (this) {
			return description;
		}
	}

	public Location getLocation() {
		synchronized (this) {
			return location;
		}
	}

	public String getPicture() {
		synchronized (this) {
			return picture;
		}
	}

	public ArrayList<Comment> getComments() {
		synchronized (this) {
			ArrayList<Comment> copy = new ArrayList<>();
			copy.addAll(comments);
			return comments;
		}
	}

	public void setName(String name) {
		synchronized (this) {
			if (name != null && !name.isEmpty())
				this.name = name;
		}
	}

	public void setDescription(String description) {
		synchronized (this) {
			if (description != null && !description.isEmpty())
				this.description = description;
		}
	}

	public void setLocation(Location location) {
		synchronized (this) {
			if (location != null)
				this.location = location;
		}
	}

	public void setPicture(String picture) {
		synchronized (this) {
			if (picture != null && !picture.isEmpty())
				this.picture = picture;
		}
	}

	public String getAuthorEmail() {
		synchronized (this) {
			return authorEmail;
		}
	}

	public User getAuthor() {
		synchronized (this) {
			return UsersManager.getInstance().getUserFromCache(this.authorEmail);
		}
	}

}
