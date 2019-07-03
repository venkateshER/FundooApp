package com.bridgeit.user.controller;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeit.note.model.Note;
import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;

import com.bridgeit.user.model.User;
import com.bridgeit.user.model.UserResponse;
import com.bridgeit.user.service.AmazonClient;
import com.bridgeit.user.service.UserService;
import com.bridgeit.utility.Response;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins="*",allowedHeaders="*",exposedHeaders= {"jwtTokens"})
//@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AmazonClient amazonClient;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> userRegister(@RequestBody UserDto user, HttpServletRequest request) {
		UserResponse response = userService.register(user, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/activation/{token}")
	public ResponseEntity<UserResponse> userRegisterActivation(@PathVariable String token) {
		UserResponse response = userService.registerActivation(token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/forgotpassword")
	public ResponseEntity<UserResponse> userForgotPassword(@RequestBody ForgotPasswordDto forgotdto,
			HttpServletRequest request) {
		UserResponse response = userService.forgotPassword(forgotdto, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/setPassword")
	public ResponseEntity<UserResponse> resetPassword(@RequestBody SetPasswordDto setPassDto, @RequestHeader String token) {
		UserResponse response = userService.setPassword(setPassDto, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<UserResponse> userLogin(@RequestBody LoginDto loginDto, HttpServletResponse httpResponse) {

		UserResponse response = userService.login(loginDto, httpResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public List<User> userGetAll() {
		List<User> user = userService.getAll();
		return user;
	}

	@DeleteMapping("/delete")
	public ResponseEntity<UserResponse> delete(@RequestHeader String token) {
		UserResponse response = userService.delete(token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	public UserController(AmazonClient amazonClient) {
//		this.amazonClient = amazonClient;
//	}

	@PostMapping("/uploadFile")
	public ResponseEntity<UserResponse> uploadFile(@RequestPart(value = "file") MultipartFile file, String token) {
		// return this.amazonClient.uploadFile(file, token);
		UserResponse response = amazonClient.uploadFile(file, token);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@DeleteMapping("/deleteFile")
	public ResponseEntity<UserResponse> deleteFile(@RequestHeader String fileName, @RequestHeader String token) {
		// return this.amazonClient.deleteFileFromS3Bucket(fileName,token);
		UserResponse response = amazonClient.deleteFileFromS3Bucket(fileName, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getProfile")
	public URL getProfilePic(@RequestHeader String token)
	{
		URL profile=amazonClient.getProfile(token);
		return profile;
	}
	
//	@GetMapping("redisToken")
//	public User redisData(@RequestHeader  String token) {
//	    return userService.getRedisData(token);
//	}

}
