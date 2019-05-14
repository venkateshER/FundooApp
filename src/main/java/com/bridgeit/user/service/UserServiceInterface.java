package com.bridgeit.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;

import com.bridgeit.user.model.User;
import com.bridgeit.utility.Response;

public interface UserServiceInterface {
	public List<User> getAll();

	public Response register(UserDto userDto, HttpServletRequest request);

	public Response forgotPassword(ForgotPasswordDto forgotdto, HttpServletRequest request);

	public Response login(LoginDto loginDto, HttpServletResponse httpResponse);

	public Response setPassword(SetPasswordDto setPassDto, String token);

	public Response delete(String token);

	public Response registerActivation(String token);

	public void registerActivationMail(User user, HttpServletRequest request);

}
