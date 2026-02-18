package com.ffpalu.concessionaria.config;

import com.ffpalu.concessionaria.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationProvider authenticationProvider;



	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
						.csrf(AbstractHttpConfigurer::disable)
						.authorizeHttpRequests(auth -> auth
														.requestMatchers("/v3/api-docs/**").permitAll()
														.requestMatchers("/swagger-ui.html").permitAll()
														.requestMatchers("/swagger-ui/**").permitAll()

														.requestMatchers("/api/auth/login").permitAll()

														.requestMatchers("/api/auth/**").authenticated()
														.requestMatchers("/api/user/**").authenticated()
														.requestMatchers("/api/customer/**").authenticated()

														.anyRequest().authenticated()
										)
						.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.authenticationProvider(authenticationProvider)
						.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}


}
