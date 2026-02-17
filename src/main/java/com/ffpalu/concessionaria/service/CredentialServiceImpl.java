package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.repository.CredentialRepository;
import com.ffpalu.concessionaria.service.interfaces.CredentialService;
import com.ffpalu.concessionaria.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

	private final CredentialRepository credentialRepository;
	private final PasswordEncoder passwordEncoder;
	private final Mapper mapper;

	@Override
	public Optional<Credential> getCredentialByUsername(String username) {
		return credentialRepository.findByUsername(username);
	}

	@Override
	public Credential createCredential(CredentialRequest request, User user) {
		if (credentialRepository.existsByUsername(request.getUsername())) {
			throw new BadCredential("Username for credential already exists");
		}

		Credential newCredential = mapper.mapToCredential(request, user);


		return credentialRepository.save(newCredential);
	}

	@Override
	@Transactional
	public Credential changePassword(String username, String newPassword) {
		Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new BadCredential("User not found"));

		Credential updatedCredential = credential.withPassword(passwordEncoder.encode(newPassword));

		return credentialRepository.save(updatedCredential);
	}

	@Override
	public boolean checkIfCredentialExists(String username) {
		return credentialRepository.existsByUsername(username);
	}
}
