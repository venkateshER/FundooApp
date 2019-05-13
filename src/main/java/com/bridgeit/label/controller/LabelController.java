package com.bridgeit.label.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.label.Dto.LabelDto;
import com.bridgeit.label.model.Label;
import com.bridgeit.label.service.LabelService;
import com.bridgeit.user.model.Response;

@RequestMapping("/label")
@RestController
//@Controller
public class LabelController {
	
	@Autowired
	private LabelService labelService;
	
	@PostMapping("/create")
	ResponseEntity<Response>createlabel(@RequestBody LabelDto labeldto,@RequestHeader String token)
	{
		Response response=labelService.create(labeldto, token);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	ResponseEntity<Response>delete(@RequestHeader long id,@RequestHeader String token)
	{
		Response response=labelService.delete(id, token);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@PutMapping("/update")
	ResponseEntity<Response>update(@RequestHeader long id,@RequestBody LabelDto labelDto,@RequestHeader String token)
	{
		Response response=labelService.update(id, labelDto, token);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/getAllLabels")
	ResponseEntity<Object>getLabels(@RequestHeader String token)
	{
		List<Label> labels=labelService.getAllLabels(token);
		return new ResponseEntity<>(labels,HttpStatus.OK);
	}
	@PostMapping("/addNotes")
	ResponseEntity<Response>addNotes(@RequestHeader long noteId,@RequestHeader String token,@RequestHeader long labelId)
	{
		Response response=labelService.addNotes(noteId, token, labelId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@PostMapping("/removeNotes")
	ResponseEntity<Response>removeNotes(@RequestHeader long noteId,@RequestHeader String token,@RequestHeader long labelId)
	{
		Response response=labelService.removeNotes(noteId, token, labelId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
