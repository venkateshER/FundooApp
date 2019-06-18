package com.bridgeit.note.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.label.model.Label;
import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.dto.ReminderDto;
import com.bridgeit.note.model.Note;
import com.bridgeit.note.service.ElasticService;
import com.bridgeit.note.service.NoteService;
import com.bridgeit.user.model.User;
import com.bridgeit.utility.Response;

import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/note")
@CrossOrigin(origins="*",allowedHeaders="*",exposedHeaders= {"jwtTokens"})
public class NoteController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody NoteDto noteDto, @RequestHeader String token) {
		Response response = noteService.create(noteDto, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PostMapping("/reminder")
	public ResponseEntity<Response> reminder(@RequestBody ReminderDto reminderDto, @RequestHeader String token,@RequestParam long id) {
		Response response = noteService.reminder(reminderDto, token, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PutMapping("/unReminder")
	public ResponseEntity<Response> unreminder(@RequestHeader String token,@RequestParam long id) {
		Response response = noteService.unreminder(token, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Response> delete(@RequestParam long id, @RequestHeader String token) {
		Response response = noteService.delete(id, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestParam long id, @RequestBody NoteDto noteDto,
			@RequestHeader String token) {
		Response response = noteService.update(id, noteDto, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/pin")
	public ResponseEntity<Response> pin(@RequestHeader String token,@RequestParam long id) {
		Response response = noteService.pin(token, id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/trash")
	public ResponseEntity<Response> trash(@RequestHeader String token,@RequestParam long id) {
		Response response = noteService.trash(token, id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/archive")
	public ResponseEntity<Response> archive(@RequestHeader String token,@RequestParam long id) {
		Response response = noteService.archive(token,id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PutMapping("/color")
	public ResponseEntity<Response>color(@RequestHeader String token, @RequestParam long id,@RequestBody String color)
	{
		Response response=noteService.color(token, id, color);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@GetMapping("/getAllNotes")
	public List<Note> getAllNotes(@RequestHeader String token) {
		List<Note> notes = noteService.getAllNotes(token);
		return notes;
	}

	@DeleteMapping("/addLabel")
	public ResponseEntity<Response> addLabel(@RequestParam long id, @RequestHeader String token,
			@RequestParam long lid) {
		Response response = noteService.addLabels(id, token, lid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/removeLabel")
	public ResponseEntity<Response> removeLabel(@RequestParam long id, @RequestHeader String token,
			@RequestParam long lid) {
		Response response = noteService.removeLabels(id, token, lid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestParam long noteId,@RequestHeader String token,@RequestParam String emailId)
	{
		Response response=noteService.addCollaborator(noteId, token, emailId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@DeleteMapping("/removeCollaborator")
	public ResponseEntity<Response> removeCollaborator(@RequestParam long noteId,@RequestHeader String token,@RequestParam String emailId)
	{
		Response response=noteService.removeCollaborator(noteId, token, emailId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/searchTitle")
	public List<Note> searchByTitle(@RequestParam String title){
		List<Note> notes=noteService.searchNoteByTitle(title);
		return notes;
	}
	@GetMapping("/getCollaborator")
	public List<User> getNotesLabel(@RequestParam long id ,@RequestHeader String token){
		List<User> col=noteService.getCollaborator(id, token);
		return col;
	}

}
