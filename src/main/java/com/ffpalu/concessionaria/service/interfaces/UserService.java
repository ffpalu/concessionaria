package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

	Page<UserResponse> getAllUser(Pageable pageable);

	Optional<User> getUserByUsername(String username);

	Optional<UserResponse> getUserByUsernameMapped(String username);

	boolean checkIfUserExists(String CF, String mail);

	User createUser(RegistrationRequest request);

	Optional<UserResponse> getUserByCF(String CF);

	Page<UserResponse> getUserByNameAndSurname(String name, String surname, Pageable pageable);

	void deactivateUser(UUID id);

	UserResponse updateUser(UUID id, RegistrationRequest request);

}
