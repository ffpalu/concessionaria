package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
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
	public boolean checkIfUserExists(String CF, String mail) {
		return userRepository.existsByCFOrEmail(CF, mail);
	}

	@Override
	public User createUser(UserDetailsRequest request) {
		if (checkIfUserExists(request.getCF(), request.getEmail())) {
			throw new UserException("User already exists");
		}

		User user = mapper.mapToUser(request);

		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserByCF(String CF) {

        return userRepository.findUserByCF(CF);
	}

	@Override
	public Page<User> getUserByNameAndSurname(String name, String surname, Pageable pageable) {
        return userRepository.findByFirstNameAndLastName(name,surname,pageable);
	}

	@Override
	public Page<User> getAllUser(Pageable pageable) {

        return userRepository.findAll(pageable);
	}


	@Override
	public void deactivateUser(UUID id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException("User to deactivate not found"));

		user.deactivate();

		userRepository.save(user);

	}

	@Override
	public User updateUser(UUID id, UserDetailsRequest request) {
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

        return userRepository.save(userToModify);
	}
}
