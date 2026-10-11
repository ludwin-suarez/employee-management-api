package com.jara.employee_management.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jara.employee_management.model.domain.AppUser;
import com.jara.employee_management.repository.AppUserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            if (appUserRepository
                    .findByUsernameAndActive("admin", true)
                    .isEmpty()) {

                AppUser user = new AppUser();

                user.setUsername("admin");
                user.setPassword(
                        passwordEncoder.encode("admin123"));
                user.setRole("ADMIN");
                user.setActive(true);

                appUserRepository.save(user);
            }
        };
    }
}