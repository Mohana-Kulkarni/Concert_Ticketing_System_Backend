package com.example.concertsystem.service.mail;

import com.example.concertsystem.entity.Mail;

public interface MailService {
    public void sendEmail(String email, Mail mail);
    public String emailVerification(String email);
    public boolean validateVerificationCode(String orgCode, String userCode);
}
