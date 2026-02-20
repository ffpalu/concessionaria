package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserMiddleware {

    Page<UserDetailsResponse> getAllUsers(Pageable pageable);

    Optional<UserDetailsResponse> getByCF(String CF);

    Page<UserDetailsResponse> getByNameAndSurname(String name, String surname, Pageable pageable);

    void deactivateUser(String id);

    UserDetailsResponse updateUser(UserDetailsRequest fieldToPatch, String ID);

}
