package com.bridgeit.utility;



public class ResponseUtil {

	public static Response getResponse(int statusCode, String statusMessage) 
	{
		Response response = new Response(statusCode, statusMessage);
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}
	public static Response getResponse(int statusCode,String token ,String statusMessage) {
		Response response = new Response(statusCode, statusMessage, statusMessage);
		response.setStatusCode(statusCode);
		response.setToken(token);
		response.setStatusMessage(statusMessage);
		return response;
	}
	
}
