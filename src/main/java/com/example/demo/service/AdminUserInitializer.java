package com.example.demo.service;

import com.example.demo.entity.Role;
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
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Default admin record created!");
            }

            if (userRepository.findByName("user").isEmpty()) { //findByUserName

                Users user = new Users();
                user.setUserName("user");
                user.setPassword(passwordEncoder.encode("user1234"));
                user.setRole(Role.USER);

                userRepository.save(user);

                System.out.println("Default user record created!");
            }
        };
    }
}
