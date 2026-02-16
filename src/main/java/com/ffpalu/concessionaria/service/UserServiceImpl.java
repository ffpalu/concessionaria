package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.UserException;
import com.ffpalu.concessionaria.repository.UserRepository;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final Mapper mapper;


	@Override
	public Optional<User> getUserByUsername(String username) {
		return userRepository.getUserByUsername(username);
	}


	@Override
	public boolean checkIfUserExists(String CF, String mail) {
		return userRepository.existsByCFOrEmail(CF, mail);
	}

	@Override
	public User createUser(RegistrationRequest request) {
		if (checkIfUserExists(request.getCf(), request.getEmail())) {
			throw new UserException("User already exists");
		}

		User user = mapper.mapToUser(request);

		return userRepository.save(user);
	}
}
