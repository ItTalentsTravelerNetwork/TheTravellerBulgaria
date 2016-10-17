package com.springframework.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springframework.functionality.ProjectManager;

@Controller
@RequestMapping(value = "/initializeManager")
public class InitializeController {

	@RequestMapping(method = RequestMethod.GET)
	public void initialize(HttpServletResponse response) {

		ProjectManager.getInstance();
		return;
	}

}
