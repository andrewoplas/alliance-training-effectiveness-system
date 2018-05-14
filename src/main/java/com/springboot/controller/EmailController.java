package com.springboot.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.service.EmailService;

@RestController
@RequestMapping("/send")
public class EmailController {
	@Autowired
	EmailService emailSender;
	
	@RequestMapping("/sendEmail")
	public String  sendmail() throws MessagingException {
		emailSender.send("andrewmoplas@gmail.com","EMAIL","TESTSETESTSE");
		
		return "email";
	}
}
