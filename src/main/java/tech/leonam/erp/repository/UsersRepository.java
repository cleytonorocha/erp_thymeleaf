package tech.leonam.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import tech.leonam.erp.model.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<UserDetails> findByLogin(String login);
    boolean existsByLogin(String login);
}
