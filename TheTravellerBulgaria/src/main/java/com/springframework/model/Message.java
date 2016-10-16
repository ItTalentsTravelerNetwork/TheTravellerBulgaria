package com.springframework.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private long id;
	private String author;
	private String recipiant;
	private String text;
	private LocalDateTime dateAndTime;

	public Message(String author, String recipiant, String text, String dateAndTime) { // without
																						// id
		setAuthor(author);
		setRecipiant(recipiant);
		setText(text);
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
	}

	public Message(long id, String author, String recipiant, String text, String dateAndTime) { // with
																								// id
		setId(id);
		setAuthor(author);
		setRecipiant(recipiant);
		setText(text);
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if (author != null)
			this.author = author;
	}

	public String getRecipiant() {
		return recipiant;
	}

	public void setRecipiant(String recipiant) {
		if (recipiant != null)
			this.recipiant = recipiant;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != null)
			this.text = text;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public String getDateAndTimeToString() {
		String dateAndTimeToString = dateAndTime.format(DATE_AND_TIME_FORMAT);
		return dateAndTimeToString;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		if (id >= 0)
			this.id = id;
	}

}
