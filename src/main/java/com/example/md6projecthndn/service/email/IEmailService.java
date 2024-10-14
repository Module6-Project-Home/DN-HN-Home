package com.example.md6projecthndn.service.email;

import com.example.md6projecthndn.service.IGenerateService;

public interface IEmailService extends IGenerateService<EmailService> {

    void sendEmail(String to, String subject, String text);
}
