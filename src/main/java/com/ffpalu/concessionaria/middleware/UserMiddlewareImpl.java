package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.middleware.interfaces.UserMiddleware;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMiddlewareImpl implements UserMiddleware {

    private final UserService userService;
    private final Mapper mapper;

    @Override
    public Page<UserDetailsResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUser(pageable);


        return users.map(mapper::mapToUserResponse);
    }

    @Override
    public Optional<UserDetailsResponse> getByCF(String CF) {
        Optional<User> userFounded = userService.getUserByCF(CF);
        return userFounded.map(mapper::mapToUserResponse);
    }

    @Override
    public Page<UserDetailsResponse> getByNameAndSurname(String name, String surname, Pageable pageable) {
        Page<User> usersFounded = userService.getUserByNameAndSurname(name, surname, pageable);
        return usersFounded.map(mapper::mapToUserResponse);
    }

    @Override
    public void deactivateUser(String id) {
        userService.deactivateUser(UUID.fromString(id));
    }

    @Override
    public UserDetailsResponse updateUser(UserDetailsRequest fieldToPatch, String ID) {
        User userUpdated = userService.updateUser(UUID.fromString(ID), fieldToPatch);
        return mapper.mapToUserResponse(userUpdated);
    }
}
