package com.tracking.userservice.mapper;

import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.userservice.entity.Role;
import com.tracking.userservice.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDtoRpc toUserDtoRpc(User user) {
        return UserDtoRpc.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
