package com.bridgeit.label.service;

import java.util.List;
import java.util.Set;

import com.bridgeit.label.Dto.LabelDto;
import com.bridgeit.label.model.Label;
import com.bridgeit.utility.Response;


public interface LabelServiceInterface {

	public Response create(LabelDto label, String token);

	public Response delete(long id, String token);

	public Response update(long id, LabelDto labelDto, String token);

	public Response addNotes(long noteId, String token, long labelId);

	public Response removeNotes(long noteId, String token, long labelId);

	public Set<Label> getAllLabels(String token);

}
