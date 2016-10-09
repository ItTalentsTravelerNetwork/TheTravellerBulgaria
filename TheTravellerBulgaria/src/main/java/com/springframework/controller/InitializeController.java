package com.springframework.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springframework.functionality.ProjectManager;

@RequestMapping(value = "/initializeManager")
public class InitializeController {

	@RequestMapping(method = RequestMethod.GET)
	public void initialize(HttpServletResponse response) {
		ControllerUtils.setHeaders(response);
		ProjectManager.getInstance();
		return;
	}

	// @RequestMapping(value = "/index", method = RequestMethod.GET)
	// public void getHomePage() {
	//
	// }
}
