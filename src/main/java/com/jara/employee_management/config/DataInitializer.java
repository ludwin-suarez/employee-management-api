package com.jara.employee_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jara.employee_management.model.domain.AppUser;
import com.jara.employee_management.repository.AppUserRepository;

@Configuration
@Profile("local")
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${APP_INITIAL_ADMIN_USERNAME:admin}") String username,
            @Value("${APP_INITIAL_ADMIN_PASSWORD:}") String initialPassword) {

        return args -> {

            if (!appUserRepository.existsByUsername(username)) {

                if (initialPassword.isBlank()) {
                    throw new IllegalStateException(
                            "DEBES DEFINIR APP_INITIAL_ADMIN_PASSWORD "
                                    + "PARA CREAR EL ADMINISTRADOR INICIAL");
                }

                AppUser user = new AppUser();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(initialPassword));
                user.setRole("ADMIN");
                user.setActive(true);

                appUserRepository.save(user);
            }
        };
    }
}

/*
 * //CODIGO ANTIGUO PARA CREACIÓN AUTOMATICO DEL USUARIO Y ROL
 * import org.springframework.boot.CommandLineRunner;
 * import org.springframework.context.annotation.Bean;
 * import org.springframework.context.annotation.Configuration;
 * import org.springframework.security.crypto.password.PasswordEncoder;
 * 
 * import com.jara.employee_management.model.domain.AppUser;
 * import com.jara.employee_management.repository.AppUserRepository;
 * 
 * @Configuration
 * public class DataInitializer {
 * 
 * @Bean
 * CommandLineRunner initUsers(
 * AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
 * 
 * return args -> {
 * 
 * if (appUserRepository
 * .findByUsernameAndActive("admin", true)
 * .isEmpty()) {
 * 
 * AppUser user = new AppUser();
 * 
 * user.setUsername("admin");
 * user.setPassword(
 * passwordEncoder.encode("admin123"));
 * user.setRole("ADMIN");
 * user.setActive(true);
 * 
 * appUserRepository.save(user);
 * }
 * };
 * }
 * }
 * 
 */