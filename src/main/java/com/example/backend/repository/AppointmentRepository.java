package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Appointment;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // existing method
    List<Appointment> findByUserEmail(String userEmail);

    // ✅ ADD THIS (for preventing double booking)
    boolean existsByDoctorNameAndDateAndTime(
        String doctorName,
        String date,
        String time
    );
}