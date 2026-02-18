package com.ffpalu.concessionaria.utils;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationCustomer;
import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationSellerDetailsRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

	private final PasswordEncoder passwordEncoder;

	public UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
						.id(user.getId())
						.email(user.getEmail())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.CF(user.getCF())
						.build();
	}

	public User mapToUser(RegistrationRequest request) {
		return User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.CF(request.getCF())
				.email(request.getEmail())
				.build();
	}

	public AuthResponse mapToLoginResponse(String token, User user) {
		return AuthResponse.builder()
						.token(token)
						.type("Bearer")
						.user(mapToUserResponse(user))
						.build();
	}

	public Credential mapToCredential(CredentialRequest request, User user) {
		return Credential.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(request.getRole())
				.user(user)
				.build();
	}

	public Seller mapToSeller(RegistrationSellerDetailsRequest request, User user) {
		return Seller.builder()
				.employeeCode(request.getEmployeeCode())
				.businessPhone(request.getBusinessPhone())
				.hireDate(request.getHireDate())
				.user(user)
				.build();
	}

	public Customer mapToCustomer(RegistrationCustomer customer) {
		return Customer.builder()
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.email(customer.getEmail())
				.CF(customer.getCF())
				.build();
	}

	public CustomerResponse mapToCustomerResponse(Customer customer) {
		return CustomerResponse.builder()
				.id(customer.getId())
				.firstName(customer.getFirstName())
				.lastName(customer.getEmail())
				.CF(customer.getCF())
				.numberOfSales(customer.getSalesCount())
				.build();
	}

}
