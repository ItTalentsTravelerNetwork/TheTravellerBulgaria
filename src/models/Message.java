package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private String author;
	private String recipiant;
	private String text;
	private LocalDateTime dateAndTime;

	public Message(String author, String recipiant, String text, String dateAndTime) {
		setAuthor(author);
		setRecipiant(recipiant);
		setText(text);
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
	}

	public synchronized String getAuthor() {
		return author;
	}

	public synchronized void setAuthor(String author) {
		if (author != null)
			this.author = author;
	}

	public synchronized String getRecipiant() {
		return recipiant;
	}

	public synchronized void setRecipiant(String recipiant) {
		if (recipiant != null)
			this.recipiant = recipiant;
	}

	public synchronized String getText() {
		return text;
	}

	public synchronized void setText(String text) {
		if (text != null)
			this.text = text;
	}

	public synchronized void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public synchronized LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public synchronized String getDateAndTimeToString() {
		String dateAndTimeToString = dateAndTime.format(DATE_AND_TIME_FORMAT);
		return dateAndTimeToString;
	}

}
