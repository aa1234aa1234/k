package com.example.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public class Message {
	private String sender;
	private String message;
	public Message() {
		
	}
	public Message(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	public enum MessageType {
		JOIN, CHAT;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
