package com.ffpalu.concessionaria.utils;

import com.ffpalu.concessionaria.dto.request.*;
import com.ffpalu.concessionaria.dto.response.*;
import com.ffpalu.concessionaria.dto.support.SaleUnwrappedDTO;
import com.ffpalu.concessionaria.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

	private final PasswordEncoder passwordEncoder;

	public UserDetailsResponse mapToUserResponse(User user) {
		return UserDetailsResponse.builder()
						.id(user.getId())
						.email(user.getEmail())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.CF(user.getCF())
						.createdAt(user.getCreatedAt())
						.updatedAt(user.getUpdatedAt())
						.build();
	}

	public User mapToUser(UserDetailsRequest request) {
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

	public Seller mapToSeller(SellerDetailsRequest request, User user) {
		return Seller.builder()
				.employeeCode(request.getEmployeeCode())
				.businessPhone(request.getBusinessPhone())
				.hireDate(request.getHireDate())
				.user(user)
				.build();
	}

	public Customer mapToCustomer(CustomerRequest customer) {
		return Customer.builder()
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.email(customer.getEmail())
				.CF(customer.getCF())
				.build();
	}

	public SellerDetailsResponse mapToSellerDetailsResponse(Seller seller) {
		return SellerDetailsResponse.builder()
				.id(seller.getId())
				.employeeCode(seller.getEmployeeCode())
				.hireDate(seller.getHireDate())
				.businessPhone(seller.getBusinessPhone())
				.userDetails(mapToUserResponse(seller.getUser()))
				.build();
	}

	public CustomerResponse mapToCustomerResponse(Customer customer) {
		return CustomerResponse.builder()
				.id(customer.getId())
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.email(customer.getEmail())
				.CF(customer.getCF())
				.numberOfSales(customer.getSalesCount())
				.build();
	}

	public VehicleResponse mapToVehicleResponse(Vehicle vehicle) {
		return VehicleResponse.builder()
				.plate(vehicle.getPlate())
				.model(vehicle.getModel())
				.brand(vehicle.getBrand())
				.used(vehicle.getUsed())
				.year(vehicle.getYear())
				.id(vehicle.getId())
				.sold(vehicle.getSale() != null)
				.createdAt(vehicle.getCreatedAt())
				.updatedAt(vehicle.getUpdatedAt())
				.numberOfKilometers(vehicle.getNumberOfKilometers())
				.build();

	}


	public Vehicle mapToVehicle(VehicleRequest vehicleRequest) {
		return Vehicle.builder()
				.plate(vehicleRequest.getPlate())
				.model(vehicleRequest.getModel())
				.brand(vehicleRequest.getBrand())
				.used(vehicleRequest.getUsed())
				.year(vehicleRequest.getYear())
				.numberOfKilometers(vehicleRequest.getNumberOfKilometers())
				.build();
	}

	public Sale mapToSale(SaleUnwrappedDTO sale) {
		return Sale.builder()
				.sellDate(sale.getSellDate())
				.price(sale.getPrice())
				.sellerId(sale.getSeller())
				.customerId(sale.getCustomer())
				.vehicleId(sale.getVehicle())
				.build();
	}

	public SaleResponse mapToSaleResponse(Sale sale, SellerDetailsResponse seller, CustomerResponse customer, VehicleResponse vehicle ){
		return SaleResponse.builder()
				.id(sale.getId())
				.sellDate(sale.getSellDate())
				.price(sale.getPrice())
				.seller(seller)
				.customer(customer)
				.vehicle(vehicle)
				.build();
	}

	public SaleResponse mapToSaleResponse(Sale sale){
		return SaleResponse.builder()
				.id(sale.getId())
				.sellDate(sale.getSellDate())
				.price(sale.getPrice())
				.seller(mapToSellerDetailsResponse(sale.getSeller()))
				.customer(mapToCustomerResponse(sale.getCustomer()))
				.vehicle(mapToVehicleResponse(sale.getVehicle()))
				.build();
	}



}
