package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationSellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;

public interface SellerService {

    public boolean checkIfPresent(String employeeCode);

    public Seller createSeller(RegistrationSellerDetailsRequest request, User user);

}
