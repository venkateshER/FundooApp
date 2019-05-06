package com.bridgeit.utility;

import com.bridgeit.user.model.Response;

public class ResponseUtil {

	public static Response getResponse(int statusCode, String statusMessage) {
		Response response = new Response();
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}
	public static Response getResponse(int statusCode,String token ,String statusMessage) {
		Response response = new Response();
		response.setStatusCode(statusCode);
		response.setToken(token);
		response.setStatusMessage(statusMessage);
		return response;
	}
	
}
