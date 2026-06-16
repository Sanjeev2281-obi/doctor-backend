package com.example.backend.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.backend.entity.Appointment;
import com.example.backend.entity.Doctor;
import com.example.backend.entity.User;
import com.example.backend.entity.PaymentStatus;
import com.example.backend.repository.AppointmentRepository;
import com.example.backend.repository.DoctorRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        // Resolve Doctor and Patient
        Doctor doctor = null;
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        }

        User patient = null;
        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            patient = userRepository.findById(appointment.getPatient().getId()).orElse(null);
        } else if (appointment.getUserEmail() != null) {
            List<User> patients = userRepository.findByEmail(appointment.getUserEmail());
            if (!patients.isEmpty()) {
                patient = patients.get(0);
            }
        }

        // Set references and backwards-compatible columns
        if (doctor != null) {
            appointment.setDoctor(doctor);
            appointment.setDoctorName(doctor.getName());
            appointment.setDoctorImage(doctor.getImage());
        }
        if (patient != null) {
            appointment.setPatient(patient);
            appointment.setUserEmail(patient.getEmail());
        }

        // Check double booking
        boolean alreadyBooked = false;
        if (doctor != null) {
            alreadyBooked = appointmentRepository.existsByDoctorIdAndDateAndTime(
                doctor.getId(),
                appointment.getDate(),
                appointment.getTime()
            );
        } else {
            alreadyBooked = appointmentRepository.existsByDoctorNameAndDateAndTime(
                appointment.getDoctorName(),
                appointment.getDate(),
                appointment.getTime()
            );
        }

        if (alreadyBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("This slot is already booked");
        }

        appointment.setPaymentStatus(PaymentStatus.UNPAID);
        Appointment saved = appointmentRepository.save(appointment);

        // Email sending
        try {
            emailService.sendAppointmentConfirmation(
                appointment.getUserEmail(),
                appointment.getDoctorName(),
                appointment.getDate(),
                appointment.getTime()
            );
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/pay/{id}")
    public String payAppointment(@PathVariable Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            return "Appointment not found";
        }

        Appointment appointment = appointmentOpt.get();
        appointment.setPaymentStatus(PaymentStatus.PAID);
        appointmentRepository.save(appointment);

        return "Payment successful";
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
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