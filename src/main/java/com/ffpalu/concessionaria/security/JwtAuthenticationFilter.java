package com.ffpalu.concessionaria.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeaderRequest = request.getHeader("Authorization");
		String jwtToken;
		String usernameToken;

		if(authHeaderRequest == null || !authHeaderRequest.startsWith("Bearer ")) {
			filterChain.doFilter(request,response);
		}
		else {

			jwtToken = authHeaderRequest.substring(7);
			usernameToken = jwtService.extractUsernameFromToken(jwtToken);

			if(usernameToken == null || SecurityContextHolder.getContext().getAuthentication() != null) {
				filterChain.doFilter(request,response);
			}
			else {
				UserDetails userDetails = userDetailsService.loadUserByUsername(usernameToken);

				if (jwtService.isTokenValid(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

				filterChain.doFilter(request,response);

			}


		}





	}
}
