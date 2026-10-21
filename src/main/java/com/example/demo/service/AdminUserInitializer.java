package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(
            UserDetailsRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByName("admin").isEmpty()) { //findByUserName

                Users admin = new Users();
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);

                System.out.println("Default admin user created!");
            }
        };
    }
}
