package com.example.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the SMTP server details
        mailSender.setHost("smtp.gmail.com"); // SMTP server for Gmail
        mailSender.setPort(587); // Port for STARTTLS

        // Use your Gmail email address and app password
        mailSender.setUsername("vedantikotkar45@gmail.com"); // Your email address
        mailSender.setPassword("zcha dcru nbxz paiy"); // Your app password

        // Set JavaMail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.debug", "true"); // Enable debug output

        // Trust all certificates for SSL connections
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender; // Return the configured JavaMailSender instance
    }
}
