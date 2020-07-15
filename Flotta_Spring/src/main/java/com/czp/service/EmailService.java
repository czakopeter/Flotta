package com.czp.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.czp.entity.User;

import ch.qos.logback.core.db.dialect.MsSQLDialect;

@Service
public class EmailService extends ServiceWithMsg {
    private final Log log = LogFactory.getLog(this.getClass());
    
    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;
	
    @Autowired
    public JavaMailSender emailSender;

	
	public boolean sendMessage(User user) {
	  MimeMessage message = emailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(user.getEmail());
      helper.setSubject("Verify");
      helper.setText("<a href=\"localhost:8080/activation/" + user.getPasswordRenewerKey() + "\">Activate here</a><br>Initial password: " + user.getPassword(), true);
      emailSender.send(message);
    } catch (MessagingException e) {
      log.error("Multipart creation failed " + e);
    } catch (MailSendException e) {
      log.error("Failure when sending the message " + e);
      return false;
    }
    return true;
	}
	
	private MimeMessage createMessage(User to, String subject, String msg) {
	  MimeMessage message = emailSender.createMimeMessage();
	  try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to.getEmail());
      helper.setSubject("Verify");
      helper.setText("<a href=\"localhost:8080/activation/" + to.getPasswordRenewerKey() + "\">Activate here</a><br>Initial password: " + to.getPassword(), true);
	  }catch (MessagingException e) {
	    log.error("Multipart creation failed " + e);
	    return null;
    }
	  return message;
	}
	
	public boolean sendEmailAboutPasswordChange(boolean success) {
	  Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
	  User user = (User)auth.getPrincipal();
	  MimeMessage message = createMessage(user, "Password change", success ? successPassworChange() : failedPasswordChange());
	  if(message == null) {
	    return false;
	  }
	  try {
	    emailSender.send(message);
    } catch (MailSendException e) {
      return false;
    }
	  
	  return true;
	}
	
	private String successPassworChange() {
	  return "Password change has been success!";
	}
	
	private String failedPasswordChange() {
	  return "Password change has been failed!";
	}
	
}
