package com.bridgeit.utility;

public class Responses {
	private int statusCode;
	private String statusMessage;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	@Override
	public String toString() {
		return "Responses [statusCode=" + statusCode + ", statusMessage=" + statusMessage + "]";
	}
	
	

}
