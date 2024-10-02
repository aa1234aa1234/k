package com.example.Test;

import org.springframework.context.annotation.Bean;

public class ChatRepository {
	private Message[] message = new Message[1000];
	private int messagecount = 0;
	public ChatRepository() {
		
	}
	public ChatRepository(Message[] message, int messagecount) {
		this.message = message;
		this.messagecount = messagecount;
	}
	public Message[] getMessage() {
		return message;
	}
	public void setMessage(Message[] message) {
		this.message = message;
	}
	public int getMessagecount() {
		return messagecount;
	}
	public void setMessagecount(int playercount) {
		this.messagecount = playercount;
	}
	public void addMessage(Message m) {
		synchronized(message) {
			message[messagecount++] = m;
		}
	}
}
