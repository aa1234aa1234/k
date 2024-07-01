package com.test.test;

import java.util.ArrayList;

public class MessageLog {
	private static ArrayList<Message> log = new ArrayList<>();
	public static ArrayList<Message> getLog() {
		return log;
	}
}
