package com.sec.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sec.entity.User;

@Service
public class EmailService {
    private final Log log = LogFactory.getLog(this.getClass());
    
    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;
	
//    private JavaMailSender javaMailSender;
//
//    @Autowired
//    public void setJavaMailSender(JavaMailSender javaMailSender) {
//      this.javaMailSender = javaMailSender;
//    }

    @Autowired
    public JavaMailSender emailSender;

	public void sendMessage(User user) {
	  //TODO  username or password not accepted
		String email = user.getEmail();
		try {
		  SimpleMailMessage message = new SimpleMailMessage();
//			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Checker");
			message.setText("Dear " + email + "! \n \n Please verify your profile and change password!"
			    + " \n \n <a href=\"localhost:8080/verifyAndChangePassword/" + user.getPasswordRenewerKey() + "\">Click here<a/>");
			
			emailSender.send(message);
		} catch (Exception e) {
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + "  " + e);
		}
		
	}
	
	
}
