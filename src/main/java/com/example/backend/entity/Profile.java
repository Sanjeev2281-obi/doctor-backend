package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String image;

    @Embedded
    private Address address;

    // Constructors
    public Profile() {}
    public Profile(String name, String email, String phone, String gender, String dob, String image, Address address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.image = image;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    @Embeddable
    public static class Address {
        private String line1;
        private String line2;

        public Address() {}
        public Address(String line1, String line2) {
            this.line1 = line1;
            this.line2 = line2;
        }

        public String getLine1() { return line1; }
        public void setLine1(String line1) { this.line1 = line1; }
        public String getLine2() { return line2; }
        public void setLine2(String line2) { this.line2 = line2; }
    }
}
