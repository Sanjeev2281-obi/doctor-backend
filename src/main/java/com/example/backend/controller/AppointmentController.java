package com.example.backend.controller;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entity.Appointment;
import com.example.backend.repository.AppointmentRepository;
import com.example.backend.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
private EmailService emailService;
   @PostMapping
public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {

    boolean alreadyBooked =
        appointmentRepository.existsByDoctorNameAndDateAndTime(
            appointment.getDoctorName(),
            appointment.getDate(),
            appointment.getTime()
        );

    if (alreadyBooked) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body("This slot is already booked");
    }

    Appointment saved = appointmentRepository.save(appointment);
     emailService.sendAppointmentConfirmation(
        appointment.getUserEmail(),
        appointment.getDoctorName(),
        appointment.getDate(),
        appointment.getTime()
    );
    return ResponseEntity.ok(saved);
}
    @PutMapping("/pay/{id}")
public String payAppointment(@PathVariable Long id) {

    Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);

    if (appointmentOpt.isEmpty()) {
        return "Appointment not found";
    }

    Appointment appointment = appointmentOpt.get();

    appointment.setPaymentStatus("paid");

    appointmentRepository.save(appointment);

    return "Payment successful";
}
    @GetMapping("/{email}")
    public List<Appointment> getAppointmentsByUser(@PathVariable String email) {
        return appointmentRepository.findByUserEmail(email);
    }
    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
        return "Appointment cancelled!";
    }
}
