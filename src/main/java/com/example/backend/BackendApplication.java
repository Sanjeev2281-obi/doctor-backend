package com.example.backend;

import com.example.backend.entity.Doctor;
import com.example.backend.entity.User;
import com.example.backend.entity.Role;
import com.example.backend.repository.DoctorRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public ApplicationRunner seedDoctor(DoctorRepository doctorRepository, UserRepository userRepository) {
		return args -> {
			// Clean up legacy doctor from users table if present
			List<User> legacyDocs = userRepository.findByEmail("doctor@gmail.com");
			if (!legacyDocs.isEmpty()) {
				userRepository.deleteAll(legacyDocs);
				System.out.println("🧹 Cleaned up legacy doctor from users table.");
			}

			// Seed Dr. Richard James
			Optional<Doctor> existingRichard = doctorRepository.findByEmail("doctor@gmail.com");
			if (existingRichard.isEmpty()) {
				Doctor doc = new Doctor();
				doc.setName("Dr. Richard James");
				doc.setEmail("doctor@gmail.com");
				doc.setPassword("doctor123");
				doc.setSpeciality("General physician");
				doc.setExperience("4 Years");
				doc.setFees(50);
				doc.setDegree("MBBS");
				doc.setAbout("Dr. James has a strong commitment to delivering comprehensive medical care, focusing on preventive medicine, early diagnosis, and effective treatment strategies.");
				doc.setImage("doc1");
				doc.setAvailable(true);
				doc.setRole(Role.DOCTOR);
				doctorRepository.save(doc);
				System.out.println("✅ Seeded Dr. Richard James successfully.");
			}

			// Seed other doctors to populate database if empty
			if (doctorRepository.count() <= 1) {
				String[] names = {
					"Dr. Emily Larson", "Dr. Sarah Patel", "Dr. Christopher Lee",
					"Dr. Jennifer Garcia", "Dr. Andrew Williams", "Dr. Christopher Davis",
					"Dr. Timothy White", "Dr. Ava Mitchell", "Dr. Jeffrey King",
					"Dr. Zoe Kelly", "Dr. Patrick Harris", "Dr. Chloe Evans",
					"Dr. Ryan Martinez", "Dr. Amelia Hill"
				};
				String[] emails = {
					"emily@gmail.com", "sarah@gmail.com", "lee@gmail.com",
					"garcia@gmail.com", "andrew@gmail.com", "davis@gmail.com",
					"white@gmail.com", "ava@gmail.com", "king@gmail.com",
					"kelly@gmail.com", "harris@gmail.com", "chloe@gmail.com",
					"ryan@gmail.com", "amelia@gmail.com"
				};
				String[] specs = {
					"Gynecologist", "Dermatologist", "Pediatricians",
					"Neurologist", "Neurologist", "General physician",
					"Gynecologist", "Dermatologist", "Pediatricians",
					"Neurologist", "Neurologist", "General physician",
					"Gynecologist", "Dermatologist"
				};
				String[] experiences = {
					"3 Years", "1 Years", "2 Years", "4 Years", "4 Years", "4 Years",
					"3 Years", "1 Years", "2 Years", "4 Years", "4 Years", "4 Years",
					"3 Years", "1 Years"
				};
				double[] feesList = { 60, 30, 40, 50, 50, 50, 60, 30, 40, 50, 50, 50, 60, 30 };
				String[] images = {
					"doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9",
					"doc10", "doc11", "doc12", "doc13", "doc14", "doc15"
				};

				for (int i = 0; i < names.length; i++) {
					Doctor doc = new Doctor();
					doc.setName(names[i]);
					doc.setEmail(emails[i]);
					doc.setPassword("doctor123");
					doc.setSpeciality(specs[i]);
					doc.setExperience(experiences[i]);
					doc.setFees(feesList[i]);
					doc.setDegree("MBBS");
					doc.setAbout("Dr. Davis has a strong commitment to delivering comprehensive medical care, focusing on preventive medicine, early diagnosis, and effective treatment strategies.");
					doc.setImage(images[i]);
					doc.setAvailable(true);
					doc.setRole(Role.DOCTOR);
					doctorRepository.save(doc);
				}
				System.out.println("✅ Seeded remaining clinic doctors successfully.");
			}
		};
	}
}
