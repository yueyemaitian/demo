package com.tmall.buy.socket.common.message;

public class Message {
	private String username;
	private String detail;
	private String friendUsername;
	public String getUsername() {
		return username;
	}
	public String getDetail() {
		return detail;
	}
	public Message(String username, String detail) {
		this(username,null,detail);
	}
	public Message(String username,String friendUsername, String detail) {
		super();
		this.username = username;
		this.friendUsername = friendUsername;
		this.detail = detail;
	}
	public String getFriendUsername() {
		return friendUsername;
	}

}
