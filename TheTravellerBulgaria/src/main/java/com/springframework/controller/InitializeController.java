package com.springframework.controller;
 
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.RestController;
 
 import com.springframework.functionality.ProjectManager;
 
 @RestController
 @RequestMapping(value = "/initializeManager")
 public class InitializeController {
 
 	@RequestMapping(method = RequestMethod.GET)
 	public void initialize() {
 		ProjectManager.getInstance();
 		return;
 	}
 
 	// @RequestMapping(value = "/index", method = RequestMethod.GET)
 	// public void getHomePage() {
 	//
 	// }
 }
