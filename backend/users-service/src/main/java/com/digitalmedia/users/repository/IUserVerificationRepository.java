package com.digitalmedia.users.repository;

import com.digitalmedia.users.model.UserVerified;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserVerificationRepository extends JpaRepository<UserVerified,Long> {
    Optional<UserVerified> findByCode(String code);
}
