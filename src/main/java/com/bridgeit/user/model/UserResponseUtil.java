package com.bridgeit.user.model;



public class UserResponseUtil {

	public static UserResponse getResponse(int statusCode, String statusMessage) 
	{
		UserResponse response = new UserResponse(statusCode, statusMessage);
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}
	public static UserResponse getResponse(int statusCode,String token ,String statusMessage) {
		UserResponse response = new UserResponse(statusCode, statusMessage, statusMessage);
		response.setStatusCode(statusCode);
		response.setToken(token);
		response.setStatusMessage(statusMessage);
		return response;
	}
	
}
