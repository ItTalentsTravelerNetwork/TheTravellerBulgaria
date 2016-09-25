package models;

import java.util.ArrayList;

public class User {

	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String description;
	private ArrayList<String> addedPlaces;
	private String profilePic;

	public User(String firstName, String lastName, String password, String email, String description,
			String profilePic) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.description = description;
		this.addedPlaces = new ArrayList<>();
		this.profilePic = profilePic;
	}

	public String getProfilePic() {
		synchronized (this) {
			return profilePic;
		}
	}

	public void setProfilePic(String profilePic) {
		synchronized (this) {
			if (profilePic != null && !profilePic.isEmpty())
				this.profilePic = profilePic;
		}
	}

	public String getFirstName() {
		synchronized (this) {
			return firstName;
		}
	}

	public String getLastName() {
		synchronized (this) {
			return lastName;
		}
	}

	public String getPassword() {
		synchronized (this) {
			return password;
		}
	}

	public String getEmail() {
		synchronized (this) {
			return email;
		}
	}

	public String getDescription() {
		synchronized (this) {
			return description;
		}
	}

	public ArrayList<String> getAddedPlaces() {
		synchronized (this) {
			ArrayList<String> copy = new ArrayList<>();
			copy.addAll(addedPlaces);
			return addedPlaces;
		}
	}

	public void setFirstName(String firstName) {
		synchronized (this) {
			if (firstName != null && !firstName.isEmpty())
				this.firstName = firstName;
		}
	}

	public void setLastName(String lastName) {
		synchronized (this) {
			if (lastName != null && !lastName.isEmpty())
				this.lastName = lastName;
		}
	}

	public void setPassword(String password) {
		synchronized (this) {
			if (password != null && !password.isEmpty())
				this.password = password;
		}
	}

	public void setEmail(String email) {
		synchronized (this) {
			if (email != null && !email.isEmpty())
				this.email = email;
		}
	}

	public synchronized void setDescription(String description) {
		synchronized (this) {
			if (description != null && !description.isEmpty())
				this.description = description;
		}
	}

	public synchronized void addPlace(String destinationName) {
		synchronized (this) {
			if (destinationName != null && !destinationName.isEmpty())
				this.addedPlaces.add(destinationName);
		}
	}

}
