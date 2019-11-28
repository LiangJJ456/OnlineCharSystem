package com.LZJ.www.po;

public class User {
	private String name;
	private String host;
	public User() {
		
	}
	public User(String name,String host) {
		super();
		this.name = name;
		this.host=host;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
