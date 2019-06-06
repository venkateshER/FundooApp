package com.bridgeit.label.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.bridgeit.utility.Response;

@RequestMapping("/label")
@RestController
@CrossOrigin(origins="*",allowedHeaders="*",exposedHeaders= {"jwtTokens"})
//@Controller
public class LabelController {

	@Autowired
	private LabelService labelService;

	@PostMapping("/create")
	ResponseEntity<Response> createlabel(@RequestBody LabelDto labeldto, @RequestHeader String token) {
		Response response = labelService.create(labeldto, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	ResponseEntity<Response> delete(@RequestHeader long labelId, @RequestHeader String token) {
		Response response = labelService.delete(labelId, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	ResponseEntity<Response> update(@RequestHeader long labelId, @RequestBody LabelDto labelDto,
			@RequestHeader String token) {
		Response response = labelService.update(labelId, labelDto, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAllLabels")
	public List getLabels(@RequestHeader String token) {
		System.out.println("djjsafjksdf");
		List<Label> labels = labelService.getAllLabels(token);
//		return new ResponseEntity<>(labels, HttpStatus.OK);
		return labels;
	}

//	@PutMapping("/addNotes")
//	ResponseEntity<Response> addNotes(@RequestHeader long noteId, @RequestHeader String token,
//			@RequestHeader long labelId) {
//		Response response = labelService.addNotes(noteId, token, labelId);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/removeNotes")
//	ResponseEntity<Response> removeNotes(@RequestHeader long noteId, @RequestHeader String token,
//			@RequestHeader long labelId) {
//		Response response = labelService.removeNotes(noteId, token, labelId);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
}
