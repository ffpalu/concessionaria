package com.ffpalu.concessionaria.utils;

import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.entity.User;

public class Mapper {
	public static UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
						.id(user.getId())
						.email(user.getEmail())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.build();
	}

	public static AuthResponse mapToLoginResponse(String token, User user) {
		return AuthResponse.builder()
						.token(token)
						.type("Bearer")
						.user(mapToUserResponse(user))
						.build();
	}
}
