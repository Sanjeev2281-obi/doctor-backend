package com.example.backend.controller;

import com.example.backend.entity.Appointment;
import com.example.backend.entity.Doctor;
import com.example.backend.entity.PaymentStatus;
import com.example.backend.repository.AppointmentRepository;
import com.example.backend.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctorProfile(@PathVariable Long id, @RequestBody Doctor updatedDetails) {
        return doctorRepository.findById(id).map(existing -> {
            existing.setName(updatedDetails.getName());
            existing.setExperience(updatedDetails.getExperience());
            existing.setFees(updatedDetails.getFees());
            existing.setAbout(updatedDetails.getAbout());
            existing.setAvailable(updatedDetails.isAvailable());
            if (updatedDetails.getAddress() != null) {
                existing.setAddress(updatedDetails.getAddress());
            }
            Doctor saved = doctorRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/appointments")
    public List<Appointment> getDoctorAppointments(@PathVariable Long id) {
        return appointmentRepository.findByDoctorId(id);
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getDoctorDashboard(@PathVariable Long id) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Doctor doctor = doctorOpt.get();
        List<Appointment> appointments = appointmentRepository.findByDoctorId(id);

        long totalAppointments = appointments.size();

        // Calculate today's appointments
        String todayStr = new SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH).format(new Date());
        long todaysAppointments = appointments.stream()
                .filter(a -> todayStr.equals(a.getDate()))
                .count();

        // Calculate stats
        long paidAppointments = appointments.stream()
                .filter(a -> a.getPaymentStatus() == PaymentStatus.PAID)
                .count();

        long unpaidAppointments = appointments.stream()
                .filter(a -> a.getPaymentStatus() == PaymentStatus.UNPAID)
                .count();

        double totalRevenue = paidAppointments * doctor.getFees();

        // Count unique patients by email
        long patientCount = appointments.stream()
                .map(Appointment::getUserEmail)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("appointments", appointments);
        stats.put("totalAppointments", totalAppointments);
        stats.put("todaysAppointments", todaysAppointments);
        stats.put("totalRevenue", totalRevenue);
        stats.put("paidAppointments", paidAppointments);
        stats.put("unpaidAppointments", unpaidAppointments);
        stats.put("patientCount", patientCount);

        return ResponseEntity.ok(stats);
    }
}
