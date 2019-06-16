package com.bridgeit.note.dto;

public class ReminderDto {
	
	private String reminder;

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	@Override
	public String toString() {
		return "ReminderDto [reminder=" + reminder + "]";
	}
	
	

}
