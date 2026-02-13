package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.repository.UserRepository;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;


	@Override
	public Optional<User> getUserByUsername(String username) {
		return userRepository.getUserByUsername(username);
	}
}
