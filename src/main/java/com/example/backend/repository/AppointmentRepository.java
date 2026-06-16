package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Appointment;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserEmail(String userEmail);
    List<Appointment> findByDoctorId(Long doctorId);

    // preventing double booking
    boolean existsByDoctorNameAndDateAndTime(
        String doctorName,
        String date,
        String time
    );

    boolean existsByDoctorIdAndDateAndTime(
        Long doctorId,
        String date,
        String time
    );
}