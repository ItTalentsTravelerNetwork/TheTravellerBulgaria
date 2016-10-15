package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springframework.exceptions.InvalidDataException;
import com.springframework.functionality.CommentsManager;
import com.springframework.functionality.UsersManager;
import com.springframework.model.Comment;
import com.springframework.model.User;

@RestController
@MultipartConfig(maxFileSize = 314572800)
public class CommentController {

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	@ResponseBody
	public String addComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String text = request.getParameter("commentText");
		String destName = request.getParameter("destinationName").replaceAll("%20", " ");
		if (session.getAttribute("user") != null) {
			try {

				UsersManager.getInstance().addComment(((User) session.getAttribute("user")).getEmail(), destName, text,
						null);
			} catch (InvalidDataException e) {
				e.printStackTrace();
				return "{\"msg\" : \"Comment adding failed!\"}";
			}
			return "{\"msg\" : \"Comment adding successful!\"}";
		}
		return "{\"msg\" : \"No active user!\"}";
	}

	@RequestMapping(value = "/likeUnlikeComment", method = RequestMethod.POST)
	@ResponseBody
	public String likeUnlikeComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		if (session.getAttribute("user") != null) {
			String userEmail = ((User) session.getAttribute("user")).getEmail();
			String result = null;

			switch (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)) {
			case "Comment not liked nor disliked!":
				if (CommentsManager.getInstance().likeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment liked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			case "Comment already liked!":
				if (CommentsManager.getInstance().unlikeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment unliked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			case "Comment already disliked!":
				if (CommentsManager.getInstance().likeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment liked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			default:
				result = "{\"msg\" : \"No such comment!\"}";
				break;
			}
			return result;
		}
		return "{\"msg\" : \"No active user!\"}";
	}

	@RequestMapping(value = "/dislikeUndislikeComment", method = RequestMethod.POST)
	@ResponseBody
	public String dislikeUndislikeComment(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		if (session.getAttribute("user") != null) {
			String userEmail = ((User) session.getAttribute("user")).getEmail();
			String result = null;

			switch (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)) {
			case "Comment not liked nor disliked!":
				if (CommentsManager.getInstance().dislikeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment disliked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			case "Comment already liked!":
				if (CommentsManager.getInstance().dislikeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment disliked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			case "Comment already disliked!":
				if (CommentsManager.getInstance().undislikeComment(commentId, userEmail)) {
					result = "{\"msg\" : \"Comment undisliked!\"}";
				} else {
					result = "{\"msg\" : \"Wrong data!\"}";
				}
				break;
			default:
				result = "{\"msg\" : \"No such comment!\"}";
				break;
			}
			return result;
		}
		return "{\"msg\" : \"No active user!\"}";
	}

	@RequestMapping(value = "/showLikeButtonStatus", method = RequestMethod.GET)
	@ResponseBody
	public String showLikeButtonStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		if (session.getAttribute("user") != null) {
			String userEmail = ((User) session.getAttribute("user")).getEmail();
			if (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)
					.equals("Comment already liked!")) {
				return "{\"msg\" : \"Comment already liked!\"}";
			}
			return "{\"msg\" : \"Comment not liked!\"}";
		}
		return "{\"msg\" : \"No active user!\"}";
	}

	@RequestMapping(value = "/showDislikeButtonStatus", method = RequestMethod.GET)
	@ResponseBody
	public String showDislikeButtonStatus(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		if (session.getAttribute("user") != null) {
			String userEmail = ((User) session.getAttribute("user")).getEmail();
			if (CommentsManager.getInstance().showCommentStatus(commentId, userEmail)
					.equals("Comment already disliked!")) {
				return "{\"msg\" : \"Comment already disliked!\"}";
			}
			return "{\"msg\" : \"Comment not liked!\"}";
		}
		return "{\"msg\" : \"No active user!\"}";
	}

	@RequestMapping(value = "/getCommentById", method = RequestMethod.GET)
	@ResponseBody
	public Comment getCommentById(HttpServletRequest request, HttpServletResponse response) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		Comment comment = CommentsManager.getInstance().getCommentById(commentId);
		return comment;
	}

	@RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
	@ResponseBody
	public String uploadVideo(@RequestParam("video") MultipartFile multipartFile, HttpServletRequest request,
			HttpServletResponse response) {
		Tika tika = new Tika(); // tool extracting data from files
		try {
			String realFileType = tika.detect(multipartFile.getBytes()); // detecting
																			// the
																			// real
																			// content
																			// type
																			// of
																			// file
			if (realFileType.equals("video/quicktime") || realFileType.equals("video/mp4")
					|| realFileType.equals("application/x-matroska") || realFileType.equals("video/theora")) { // if
																												// the
																												// type
																												// matches
																												// the
																												// expected
																												// formats
				Long commentId = Long.valueOf(request.getParameter("commentId"));
				File dir = new File("commentsVideos");
				if (!dir.exists()) {
					dir.mkdir();
				}
				File video = new File(dir, commentId + "-commentVideo." + multipartFile.getOriginalFilename());
				try {
					Files.copy(multipartFile.getInputStream(), video.toPath(), StandardCopyOption.REPLACE_EXISTING);
					CommentsManager.getInstance().addVideo(commentId, video.getName());
					return "{\"msg\" : \"Success!\"}";
				} catch (IOException e) {
					System.out.println("IO exception occured");
					return "{\"msg\" : \"Failed!\"}";
				}
			} else {
				return "{\"msg\" : \"Failed!\"}";
			}

		} catch (IOException e1) {
			e1.printStackTrace();
			return "{\"msg\" : \"Failed!\"}";
		}
	}

	@RequestMapping(value = "/getVideo", method = RequestMethod.GET)
	@ResponseBody
	public void getVideo(HttpServletRequest request, HttpServletResponse response) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		String originalVideoName = request.getParameter("video");
		String commentVideoName = commentId.toString() + "-commentVideo." + originalVideoName;
		Comment comment = CommentsManager.getInstance().getCommentById(commentId);
		if (comment != null) {
			if (comment.hasVideo() && comment.getVideo().equals(commentVideoName)) {
				File video = new File("commentsVideos", commentId + "-commentVideo." + originalVideoName);
				try {
					OutputStream out = response.getOutputStream();
					Files.copy(video.toPath(), out);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping(value = "/showCommentVideoName", method = RequestMethod.GET)
	@ResponseBody
	public String showCommentVideoName(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Long commentId = Long.valueOf(request.getParameter("commentId"));
		Comment comment = CommentsManager.getInstance().getCommentById(commentId);
		if (comment != null && comment.hasVideo()) {
			String originalVideoName = comment.getVideo().split("-commentVideo.")[1];
			return originalVideoName;
		}
		return null;
	}

}
