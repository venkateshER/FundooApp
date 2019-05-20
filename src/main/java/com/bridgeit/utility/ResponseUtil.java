package com.bridgeit.utility;



public class ResponseUtil {

	public static Response getResponse(int statusCode, String statusMessage) 
	{
		Response response = new Response(statusCode, statusMessage);
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}
	
}
