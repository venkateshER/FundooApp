package com.bridgeit.utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderUtil {
	  @Autowired 
	  private JavaMailSender mailSender;
		public void mailSender(String to,String subject,String body){
			MimeMessage msg= mailSender.createMimeMessage();
			MimeMessageHelper helper;
			try {
				helper=new MimeMessageHelper(msg,true);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(body, true);
			mailSender.send(msg);
			} catch (MessagingException|MailException e) {
			
				System.out.println(""+e.getMessage());
			}
		}
}
