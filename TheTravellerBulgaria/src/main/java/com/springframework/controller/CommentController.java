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
import com.springframework.functionality.CommentsManager;
import com.springframework.functionality.UsersManager;
import com.springframework.model.Comment;
import com.springframework.model.User;

@RestController
@MultipartConfig(maxFileSize = 200000000)
public class CommentController {

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	@ResponseBody
	public String addComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String text = request.getParameter("commentText");
		String destName = request.getParameter("destinationName").replaceAll("%20", " ");
		try {
			UsersManager.getInstance().addComment(((User) session.getAttribute("user")).getEmail(), destName, text,
					null);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return "Comment adding failed!";
		}
		return "Comment adding successful!";

	}
	
	
	@RequestMapping(value = "/likeUnlikeComment", method = RequestMethod.POST)
	@ResponseBody
	public String likeUnlikeComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		String userEmail = ((User)session.getAttribute("user")).getEmail();
		String result = null;
		
		switch (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)) {
		case "Comment not liked nor disliked!":
			if (CommentsManager.getInstance().likeComment(commentId, userEmail)) {
				result = "Comment liked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		case "Comment already liked!":
			if (CommentsManager.getInstance().unlikeComment(commentId, userEmail)) {
				result = "Comment unliked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		case "Comment already disliked!":
			if (CommentsManager.getInstance().likeComment(commentId, userEmail)) {
				result = "Comment liked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		default:
			result = "No such comment!";
			break;
		}
		return result;
	}
	
	
	@RequestMapping(value = "/dislikeUndislikeComment", method = RequestMethod.POST)
	@ResponseBody
	public String dislikeUndislikeComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		String userEmail = ((User)session.getAttribute("user")).getEmail();
		String result = null;
		
		switch (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)) {
		case "Comment not liked nor disliked!":
			if (CommentsManager.getInstance().dislikeComment(commentId, userEmail)) {
				result = "Comment disliked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		case "Comment already liked!":
			if (CommentsManager.getInstance().dislikeComment(commentId, userEmail)) {
				result = "Comment disliked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		case "Comment already disliked!":
			if (CommentsManager.getInstance().undislikeComment(commentId, userEmail)) {
				result = "Comment undisliked!";
			}
			else {
				result = "Wrong data!";
			}
			break;
		default:
			result = "No such comment!";
			break;
		}
		return result;
	}
	
	@RequestMapping(value = "/showLikeButtonStatus", method = RequestMethod.GET)
	@ResponseBody
	public String showLikeButtonStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		String userEmail = ((User)session.getAttribute("user")).getEmail();
		if (CommentsManager.getInstance().showCommentStatus(commentId, userEmail).equals("Comment already liked!")) {
			return "Comment already liked!";
		}
		return "Comment not liked!";
	}
	
	@RequestMapping(value = "/showDislikeButtonStatus", method = RequestMethod.GET)
	@ResponseBody
	public String showDislikeButtonStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		String userEmail = ((User)session.getAttribute("user")).getEmail();
		if (CommentsManager.getInstance().showCommentStatus(commentId, userEmail).equals("Comment already disliked!")) {
			return "Comment already disliked!";
		}
		return "Comment not liked!";
	}
	
	@RequestMapping(value = "/getCommentById", method = RequestMethod.GET)
	@ResponseBody
	public Comment getCommentById(HttpServletRequest request, HttpServletResponse response) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		Comment comment = CommentsManager.getInstance().getCommentById(commentId);
		return comment;
	}

}
