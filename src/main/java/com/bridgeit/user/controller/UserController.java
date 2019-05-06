package com.bridgeit.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;
import com.bridgeit.user.model.Response;
import com.bridgeit.user.model.User;
import com.bridgeit.user.service.UserService;



@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Response> userRegister(@RequestBody UserDto user, HttpServletRequest request) {
		Response response = userService.register(user, request);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/activation/{token}")
	public ResponseEntity<Response> userRegisterActivation(@PathVariable String token) {
		Response response = userService.registerActivation(token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/forgotpassword")
	public ResponseEntity<Response> userForgotPassword(@RequestBody ForgotPasswordDto forgotdto,
			HttpServletRequest request) {
		Response response = userService.forgotPassword(forgotdto, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PutMapping(value = "/setPassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestBody SetPasswordDto setPassDto, @PathVariable String token) {
		Response response = userService.setPassword(setPassDto, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<Response> userLogin(@RequestBody LoginDto loginDto, HttpServletResponse httpResponse) {

		Response response = userService.login(loginDto, httpResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public List<User> userGetAll() {
		List<User> user = userService.getAll();
		return user;
	}

}
