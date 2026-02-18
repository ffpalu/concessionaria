package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.UserException;
import com.ffpalu.concessionaria.repository.UserRepository;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
	public Optional<UserResponse> getUserByUsernameMapped(String username) {

		Optional<User> user = getUserByUsername(username);

        return user.map(mapper::mapToUserResponse);
    }


	@Override
	public boolean checkIfUserExists(String CF, String mail) {
		return userRepository.existsByCFOrEmail(CF, mail);
	}

	@Override
	public User createUser(RegistrationRequest request) {
		if (checkIfUserExists(request.getCF(), request.getEmail())) {
			throw new UserException("User already exists");
		}

		User user = mapper.mapToUser(request);

		return userRepository.save(user);
	}

	@Override
	public Optional<UserResponse> getUserByCF(String CF) {

		Optional<User> user = userRepository.findUserByCF(CF);

		return user.map(mapper::mapToUserResponse);
	}

	@Override
	public Page<UserResponse> getUserByNameAndSurname(String name, String surname, Pageable pageable) {
		Page<User> listOfUsers = userRepository.findByFirstNameAndLastName(name,surname,pageable);

		return listOfUsers.map(mapper::mapToUserResponse);
	}

	@Override
	public Page<UserResponse> getAllUser(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);

		return users.map(mapper::mapToUserResponse);
	}


	@Override
	public void deactivateUser(UUID id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException("User to deactivate not found"));

		user.deactivate();

		userRepository.save(user);

	}

	@Override
	public UserResponse updateUser(UUID id, RegistrationRequest request) {
		User userToModify = userRepository.findById(id).orElseThrow(() -> new UserException("User to patched not found"));

		for(Field fieldDTO : request.getClass().getDeclaredFields()) {
			fieldDTO.setAccessible(true);

			try {
				log.info("Try to get the value");
				Object value = fieldDTO.get(request);

				log.info("Change: {} with value {}", fieldDTO.getName(), value);

				if (value != null) {
					Field fieldEntity = userToModify.getClass().getDeclaredField(fieldDTO.getName());

					fieldEntity.setAccessible(true);

					fieldEntity.set(userToModify, value);

				}


			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new UserException(e.getMessage());
			}

		}

		User modified = userRepository.save(userToModify);

		return mapper.mapToUserResponse(modified);
	}
}
