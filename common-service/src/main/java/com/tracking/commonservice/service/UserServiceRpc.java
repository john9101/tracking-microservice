package com.tracking.commonservice.service;

import com.tracking.commonservice.dto.UserDtoRpc;

import java.util.List;
import java.util.Map;

public interface UserServiceRpc {
    UserDtoRpc getUser(String username);
//    UserDtoRpc createUser(String name, String email, String username, String encodedPassword, List<String> roleNames);
    UserDtoRpc createUser(Map<String, String> requestNap, List<String> roleNames);
    UserDtoRpc updateUser(String name, String email, String username);
    boolean existUsername(String username);
    boolean existEmail(String email);
}
