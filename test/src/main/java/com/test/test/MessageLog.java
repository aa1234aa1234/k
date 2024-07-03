package com.test.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageLog {
	private List<Message> log;
	public MessageLog() {
		log = new ArrayList<>();
	}
	public List<Message> getLog() {
		return log;
	}
	public void setLog(List<Message> a) {
		log = a;
	}
}
