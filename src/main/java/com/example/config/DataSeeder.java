package com.example.config;

//DataSeeder.java


import com.example.Entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
* Seeds a single default admin account on application startup, if one
* doesn't already exist. Change the email/password below (or better,
* move them to application.properties) before deploying to production.
*/
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

@Autowired
 private  UserRepository userRepository;
@Autowired
 private  PasswordEncoder passwordEncoder;

 private static final String ADMIN_EMAIL = "admin@kokandelights.com";
 private static final String ADMIN_PASSWORD = "Admin@123"; // change this after first login

 @Override
 public void run(String... args) {
     boolean adminExists = userRepository.existsByEmail(ADMIN_EMAIL);

     if (!adminExists) {
    	 User admin = new User();

    	 admin.setName("Kokan Delights Admin");
    	 admin.setEmail(ADMIN_EMAIL);
    	 admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
    	 admin.setPhone("9999999999");
    	 admin.setRole(User.Role.ADMIN);

    	 userRepository.save(admin);

    	 userRepository.save(admin);

    	 System.out.println("Seeded default admin account -> email: " + ADMIN_EMAIL);
    	 } else {
    	     System.out.println("Admin account already exists, skipping seed.");
    	 }
 }
}