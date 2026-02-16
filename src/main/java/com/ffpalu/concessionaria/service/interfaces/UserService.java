package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.entity.User;

import java.util.Optional;

public interface UserService {

	public Optional<User> getUserByUsername(String username);

	public boolean checkIfUserExists(String CF, String mail);

	public User createUser(RegistrationRequest request);

}
