package com.employment.task.repositories;

import com.employment.task.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findById(int id);
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByBirthDateBetween(LocalDate from, LocalDate to);
}
