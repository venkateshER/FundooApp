package com.bridgeit.utility;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class Utility {

	public static String todayDate() {
//		Date date = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//		String timeanddate = dateFormat.format(date);
//		return timeanddate;
		LocalDateTime now = LocalDateTime.now();//.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(format);
		return formatDateTime;
		// Date date = new Date();
//		Date dt = new Date();
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//		String timeanddate = dateFormatter.format(dt);
//		return timeanddate;
	}

}
