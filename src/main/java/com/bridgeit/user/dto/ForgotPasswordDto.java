package com.bridgeit.user.dto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ForgotPasswordDto {
	@NotNull
	@Email
	private String emailId;
	
	@NotNull
	@Email
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
