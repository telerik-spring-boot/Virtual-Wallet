package com.telerik.virtualwallet.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }
    @Override
    public void send(String receiver, String title, String content) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiver);
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        mailMessage.setFrom("roamify55@yahoo.com");

        mailSender.send(mailMessage);
    }
}
