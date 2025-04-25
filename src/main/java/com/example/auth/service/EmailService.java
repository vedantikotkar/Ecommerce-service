//package com.example.auth.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        mailSender.send(message);
//    }
//}




package com.example.auth.service;

import com.example.auth.dto.EmailRequest;
import com.example.auth.entity.Emails;
import com.example.auth.repository.EmailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailsRepository emailsRepository;

    public void saveAndNotify(EmailRequest request) {
        // Save data to DB
        Emails emailEntity = new Emails();
        emailEntity.setName(request.getName());
        emailEntity.setEmail(request.getEmail());
        emailEntity.setPhone(request.getPhone());
        emailEntity.setMessage(request.getMessage());
        emailsRepository.save(emailEntity);

        // Send email to YOU
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("vedantikotkar45@gmail.com"); // Your email
        message.setSubject("New Contact Message from: " + request.getName());
        message.setText(
                "You received a new message:\n\n" +
                        "Name: " + request.getName() + "\n" +
                        "Email: " + request.getEmail() + "\n" +
                        "Phone: " + request.getPhone() + "\n\n" +
                        "Message:\n" + request.getMessage()
        );
        mailSender.send(message);
    }
}
