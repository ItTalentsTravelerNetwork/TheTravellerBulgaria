package com.springframework.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springframework.SpringContextProvider;
import com.springframework.functionality.ProjectManager;

@RestController
@RequestMapping(value = "/initializeManager")
public class InitializeController {

	@RequestMapping(method = RequestMethod.GET)
	public void initialize() {
		SpringContextProvider.context.getBean(ProjectManager.class);
		return;
	}

	// @RequestMapping(value = "/index", method = RequestMethod.GET)
	// public void getHomePage() {
	//
	// }
}
