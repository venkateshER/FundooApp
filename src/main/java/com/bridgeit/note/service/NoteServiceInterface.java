package com.bridgeit.note.service;

import java.util.List;

import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.model.Note;
import com.bridgeit.user.model.Response;

public interface NoteServiceInterface {
	public Response create(NoteDto noteDto,String token);
	public List<Note> getAllNotes(String token);

}
