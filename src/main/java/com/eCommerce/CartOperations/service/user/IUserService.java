package com.eCommerce.CartOperations.service.user;

import com.eCommerce.CartOperations.dto.UserDto;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.request.UserCreateRequest;
import com.eCommerce.CartOperations.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(UserCreateRequest request);

    User updateUser(UserUpdateRequest request,Long userId);

    void deleteUserById(Long userId);

    UserDto convertedToDto(User user);

    User getAuthencatedUser();
}
