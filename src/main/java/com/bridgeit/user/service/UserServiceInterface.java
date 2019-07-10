package com.bridgeit.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;

import com.bridgeit.user.model.User;
import com.bridgeit.user.model.UserResponse;


public interface UserServiceInterface {
	public List<User> getAll();

	public UserResponse register(UserDto userDto);

	public UserResponse forgotPassword(ForgotPasswordDto forgotdto);

	public UserResponse login(LoginDto loginDto);

	public UserResponse setPassword(SetPasswordDto setPassDto, String token);

	public UserResponse delete(String token);

	public UserResponse registerActivation(String token);

	public void registerActivationMail(User user);

}
