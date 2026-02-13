package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.entity.Credential;

import java.util.Optional;

public interface CredentialService {

	Optional<Credential> getCredentialByUsername(String username);

}
