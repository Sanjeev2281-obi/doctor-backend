package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentConfirmation(String toEmail, String doctor, String date, String time) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Appointment Confirmation");

        message.setText(
            "Your appointment has been booked successfully.\n\n" +
            "Doctor: " + doctor + "\n" +
            "Date: " + date + "\n" +
            "Time: " + time + "\n\n" +
            "Thank you for using our booking system."
        );

        mailSender.send(message);
    }
}