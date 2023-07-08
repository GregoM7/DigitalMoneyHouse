package com.digitalmedia.users.repository;

import com.digitalmedia.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Long> {
  Optional<User> findByName(String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByAlias(String alias);
  Optional<User> findByCvu(String cvu);
  Optional<User> findByKeycloakId(String id);
}
