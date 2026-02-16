package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;

import java.util.Optional;

public interface CredentialService {

	Optional<Credential> getCredentialByUsername(String username);

	boolean checkIfCredentialExists(String username);

	Credential createCredential(CredentialRequest request, User user);
}
