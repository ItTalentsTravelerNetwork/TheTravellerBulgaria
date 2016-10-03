package com.springframework.controller;

public class Product {
	private String name;
	private int quantity;
	private String photo;
	
	public Product() {
	}
	
	@Override
	public String toString() {
		return "Product [name=" + name + ", quantity=" + quantity + ", photo=" + photo + "]";
	}

	public Product(String name, int quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.photo = "img/Ceca.jpeg";
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
