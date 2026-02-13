package com.ffpalu.concessionaria.security;

import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Credential credentialLoaded = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredential("User not found"));

        return new User(
                credentialLoaded.getUsername(),
                credentialLoaded.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + credentialLoaded.getRole().name()))
        );
    }
}
