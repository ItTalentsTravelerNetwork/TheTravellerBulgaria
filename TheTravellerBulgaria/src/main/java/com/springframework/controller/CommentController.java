package com.springframework.controller;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springframework.exceptions.InvalidDataException;
import com.springframework.functionality.UsersManager;
import com.springframework.model.User;



@RestController
@MultipartConfig(maxFileSize = 200000000)
public class CommentController {
	
	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	@ResponseBody
	public String addComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String text = request.getParameter("commentText");
		try {
			UsersManager.getInstance().addComment(((User)session.getAttribute("user")).getEmail(), request.getParameter("destinationName"), text, null);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return "Comment adding failed!";
		}
		return "Comment adding successful!";

	}

}
