package com.bridgeit.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bridgeit.user.dto.LoginDto;
//import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.model.User;



@Component
public class EncryptUtil {
	@Autowired
	private PasswordEncoder PasswordEncoder;

	public String encryptPassword(String password) {
		return PasswordEncoder.encode(password);
	}

	public boolean isPassword(LoginDto loginDto, User user) {

		return PasswordEncoder.matches(loginDto.getPassword(),user.getPassword() );
		
	}


}
