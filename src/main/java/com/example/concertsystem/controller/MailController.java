package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Mail;
import com.example.concertsystem.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    MailService mailService;
    @PostMapping("/send/{email}")
    public void sendMail(@PathVariable String email, @RequestBody Mail mail){
        mailService.sendEmail(email,mail);
    }
}
