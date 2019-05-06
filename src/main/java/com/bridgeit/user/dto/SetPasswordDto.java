package com.bridgeit.user.dto;


import javax.validation.constraints.NotNull;

public class SetPasswordDto {
	@NotNull
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}