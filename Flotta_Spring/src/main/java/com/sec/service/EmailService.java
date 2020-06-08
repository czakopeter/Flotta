package com.sec.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	  MimeMessage message = emailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(user.getEmail());
      helper.setSubject("Verify");
      helper.setText("<a href=\"localhost:8080/verifyAndChangePassword/" + user.getPasswordRenewerKey() + "\">HERE</a>", true);
      emailSender.send(message);
    } catch (MessagingException e) {
      log.error("Hiba e-mail küldéskor az alábbi címre: " + user.getEmail() + "  " + e);
    }
	}
	
	
	
}
