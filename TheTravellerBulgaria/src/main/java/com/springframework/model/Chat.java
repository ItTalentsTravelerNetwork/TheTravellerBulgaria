package com.springframework.model;

import java.util.concurrent.CopyOnWriteArrayList;

public class Chat {

	private String user1;
	private String user2;
	private CopyOnWriteArrayList<Message> messages;

	public Chat(String user1, String user2, CopyOnWriteArrayList<Message> messages) {
		setUser1(user1);
		setUser2(user2);
		setMessages(messages);
	}

	public synchronized void addMessage(Message message) {
		if (message != null && message.getAuthor().equals(user1) && message.getRecipiant().equals(user2)) {
			messages.add(message);
		}
	}

	public synchronized String getUser1() {
		return user1;
	}

	public synchronized void setUser1(String user1) {
		if (user1 != null && !user1.isEmpty())
			this.user1 = user1;
	}

	public synchronized String getUser2() {
		return user2;
	}

	public synchronized void setUser2(String user2) {
		if (user2 != null && !user2.isEmpty())
			this.user2 = user2;
	}

	public synchronized CopyOnWriteArrayList<Message> getMessages() {
		CopyOnWriteArrayList<Message> copy = new CopyOnWriteArrayList<>();
		copy.addAll(messages);
		return copy;
	}

	public synchronized void setMessages(CopyOnWriteArrayList<Message> messages) {
		if (messages != null)
			this.messages = messages;
	}

}
