package com.springframework.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String sayHi(Model viewModel) {
		// talk with model
		
		viewModel.addAttribute("Text","Hello");
		
		return "home";
	}
	
	@RequestMapping(value="/mindex", method=RequestMethod.GET)
	public String sayBye(Model viewModel) {
		// talk with model
		Product product = new Product("Kiselo zele", 50);
		
		List<Product> products = new ArrayList<Product>();
		products.add(product);
		products.add(new Product("Kisela krastavica", 13));
		products.add(new Product("Kisel portokal", 83));
		products.add(new Product("Kisel limon", 73));
		products.add(new Product("Kisel greipfrut", 13));
		products.add(new Product("Kisel banan", 43));
		products.add(new Product("Kiselo mlqko", 33));
		
		viewModel.addAttribute("Text","Bye");
		viewModel.addAttribute("products", products);
		
		viewModel.addAttribute(new Product("", 0));
		
		return "listproducts";
	}
	
	@RequestMapping(value="/mindex", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute Product product) {
		// add to DAO
		System.out.println(product);
		
		return "listproducts";
	}
	
}
