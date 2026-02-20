package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

	Page<User> getAllUser(Pageable pageable);

	Optional<User> getUserByUsername(String username);

	boolean checkIfUserExists(String CF, String mail);

	User createUser(UserDetailsRequest request);

	Optional<User> getUserByCF(String CF);

	Page<User> getUserByNameAndSurname(String name, String surname, Pageable pageable);

	void deactivateUser(UUID id);

	User updateUser(UUID id, UserDetailsRequest request);

}
