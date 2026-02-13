package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.entity.User;

import java.util.Optional;

public interface UserService {

	public Optional<User> getUserByUsername(String username);

}
