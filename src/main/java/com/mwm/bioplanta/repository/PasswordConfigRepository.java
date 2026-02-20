package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.PasswordConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordConfigRepository extends JpaRepository<PasswordConfig, Long> {
    Optional<PasswordConfig> findFirstByStatusOrderByIdDesc(String status);
}
