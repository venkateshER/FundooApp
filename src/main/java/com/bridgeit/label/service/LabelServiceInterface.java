package com.bridgeit.label.service;

import com.bridgeit.label.Dto.LabelDto;
import com.bridgeit.label.model.Label;
import com.bridgeit.user.model.Response;

public interface LabelServiceInterface {
	
	public Response create(LabelDto label,String token);
	

}
