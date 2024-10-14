package com.example.md6projecthndn.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nguyenduckien2406@gmail.com"); // Địa chỉ email của bạn
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Override
    public Iterable<EmailService> findAll() {
        return null;
    }

    @Override
    public void save(EmailService emailService) {

    }

    @Override
    public EmailService findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}