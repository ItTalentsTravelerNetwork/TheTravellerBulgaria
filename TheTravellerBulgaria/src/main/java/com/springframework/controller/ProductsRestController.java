package com.springframework.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsRestController {
	
	@RequestMapping(value="/service/products", method=RequestMethod.GET)
	public List<Product> getProducts() {
		// read from DAO
		
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Kisela krastavica", 13));
		products.add(new Product("Kisel portokal", 83));
		products.add(new Product("Kisel limon", 73));
		products.add(new Product("Kisel greipfrut", 13));
		products.add(new Product("Kisel banan", 43));
		products.add(new Product("Kiselo mlqko", 33));
		
		return products;
	}
	
	@RequestMapping(value="/service/products/{product_id}", method=RequestMethod.GET)
	public Product getProduct(@PathVariable(value="product_id") Integer productId) {
		// read from DAO concrente object with id
		
		return new Product("Kiselo zele", productId);
	}
	

}
