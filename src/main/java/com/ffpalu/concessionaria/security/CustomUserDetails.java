package com.ffpalu.concessionaria.security;


import com.ffpalu.concessionaria.entity.Credential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final Credential credential;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + credential.getRole()));
	}

	@Override
	public String getPassword() {
		return credential.getPassword();
	}

	@Override
	public String getUsername() {
		return credential.getUsername();
	}

}
