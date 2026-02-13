package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.repository.CredentialRepository;
import com.ffpalu.concessionaria.service.interfaces.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

	private final CredentialRepository credentialRepository;

	@Override
	public Optional<Credential> getCredentialByUsername(String username) {
		return credentialRepository.findByUsername(username);
	}
}
