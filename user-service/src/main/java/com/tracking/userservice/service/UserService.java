package com.tracking.userservice.service;

import com.tracking.userservice.dto.request.CreateUserRequest;
import com.tracking.userservice.dto.request.UpdateUserRequest;
import com.tracking.userservice.dto.response.*;

import java.util.Locale;

public interface UserService {
    GotUserResponse getUser(Long id, Locale locale);
    UpdatedUserResponse updateUser(Long id, UpdateUserRequest request, Locale locale);
    CreatedUserResponse createUser(CreateUserRequest request, Locale locale);
    DeletedUserResponse deleteUser(Long id, Locale locale);
    GotAllUsersResponse getAllUsers(Locale locale);
}
