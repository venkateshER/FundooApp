package com.bridgeit.user.model;

public class Response {

	int statusCode;
	String token;
	String statusMessage;

	public Response() {

	}

	public Response(int statusCode, String token, String statusMessage) {
		super();
		this.statusCode = statusCode;
		this.token = token;
		this.statusMessage = statusMessage;
	}

	public Response(int statusCode, String token) {
		super();
		this.statusCode = statusCode;
		this.token = token;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
