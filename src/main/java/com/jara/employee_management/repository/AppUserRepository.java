package com.jara.employee_management.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jara.employee_management.model.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameAndActive(
            String username,
            Boolean active);

    boolean existsByUsername(String username);
}