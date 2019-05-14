package com.bridgeit.utility;

public class Response {

	private int statusCode;
	private String token;
	private String statusMessage;



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

	public Response(int statusCode, String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

	public Response(int statusCode, String token, String statusMessage) {
		this.statusCode = statusCode;
		this.token = token;
		this.statusMessage = statusMessage;
	}

	@Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", token=" + token + ", statusMessage=" + statusMessage + "]";
	}



	
}
