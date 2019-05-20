package com.bridgeit.note.service;

import java.util.List;
import java.util.Set;

import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.model.Note;
import com.bridgeit.utility.Response;


public interface NoteServiceInterface {
	public Response create(NoteDto noteDto, String token);

	public Response delete(long id, String token);

	public List<Note> getAllNotes(String token);

	public Response update(long id, NoteDto noteDto, String token);

	public Response pin(String token, long noteId);

	public Response color(String token, long noteId, String color);

	public Response trash(String token, long noteId);

	public Response archive(String token, long noteId);

	public Response addLabels(long noteId, String token, long labelId);

	public Response removeLabels(long noteId, String token, long labelId);

}
