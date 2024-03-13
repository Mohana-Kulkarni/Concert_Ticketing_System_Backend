package com.example.concertsystem.service.mail;

import com.example.concertsystem.entity.Mail;
import com.example.concertsystem.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public void sendEmail(String email, Mail mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mail.getSubject());
        simpleMailMessage.setText(mail.getMessage());
        simpleMailMessage.setTo(email);
        javaMailSender.send(simpleMailMessage);
    }
    @Override
    public String emailVerification(String email){
        Mail mail = new Mail();
        String code = HelperUtil.generateVerificationCode(5);
        String subject = "Confirmation instructions";
        String message = String.format("""
                Hi %s,
                Welcome to TKet, From Beat Drops to Ticket Shops: Where Every Concert Journey Begins.
                To complete the account verification, Verification Code: %s""",email,code);
        mail.setSubject(subject);
        mail.setMessage(message);
        sendEmail(email,mail);
        return code;
    }
    @Override
    public boolean validateVerificationCode(String orgCode, String userCode){
        return orgCode.equals(userCode);
    }

}
