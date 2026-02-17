package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationSellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;

public interface SellerService {

    boolean checkIfPresent(String employeeCode);

    Seller createSeller(RegistrationSellerDetailsRequest request, User user);

}
