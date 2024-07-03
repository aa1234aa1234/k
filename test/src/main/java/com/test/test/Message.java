package com.test.test;

public class Message {
	private String sender,msg;
	public Message() {
		
	}
	public Message(String sender, String msg) {
		this.sender = sender;
		this.msg = msg;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
