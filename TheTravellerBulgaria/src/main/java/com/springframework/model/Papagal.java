package com.springframework.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Papagal {

	private String name;
	private int age;
	private Address address;
	private List<Papagal> friends = new ArrayList<>();
	
	public Papagal(String name, int age, Address address) {
		this.name = name;
		this.age = age;
		this.address = address;
		friends = Arrays.asList(new Papagal("Poli", 3), new Papagal("Moli", 4), new Papagal("Choli", 5));
	}
	
	public Papagal(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papagal other = (Papagal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Papagal> getFriends() {
		return Collections.unmodifiableList(friends);
	}

	public void setFriends(List<Papagal> friends) {
		this.friends = friends;
	}
	
	
}
