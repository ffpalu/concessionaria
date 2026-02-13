package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	@Query("SELECT u FROM User u JOIN u.credential c WHERE c.username = :username")
	Optional<User> getUserByUsername(String username);

	Optional<User> findUserByCF(String CF);

}
