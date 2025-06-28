package com.tracking.userservice.service.impl;

import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.service.UserServiceRpc;
import com.tracking.userservice.entity.Role;
import com.tracking.userservice.entity.User;
import com.tracking.userservice.mapper.UserMapper;
import com.tracking.userservice.repository.RoleRepository;
import com.tracking.userservice.repository.UserRepository;
import com.tracking.userservice.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@DubboService
@RequiredArgsConstructor
public class UserServiceRpcImpl implements UserServiceRpc {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDtoRpc getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserMapper.toUserDtoRpc(user);
        }
        return null;
    }

//    @Override
//    public UserDtoRpc createUser(String name, String email, String username, String encodedPassword, List<String> roleNames) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(encodedPassword);
//        user.setEmail(email);
//        user.setName(name);
//        List<Role> roles = new ArrayList<>();
//        for (String roleName : roleNames) {
//            Optional<Role> optionalRole = roleRepository.findByName(roleName);
//            optionalRole.ifPresent(roles::add);
//        }
//        user.setRoles(roles);
//        return UserMapper.toUserDtoRpc(userRepository.save(user));
//    }

    @Override
    public UserDtoRpc createUser(Map<String, String> requestNap, List<String> roleNames) {
        User user = new User();
        user.setUsername(requestNap.get("username"));
        user.setPassword(requestNap.get("encodedPassword"));
        user.setEmail(requestNap.get("email"));
        user.setName(requestNap.get("name"));
        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) {
            List.of(roleRepository.findByName(ConstantUtil.ROLE_USER))
                    .forEach(optionalRole -> optionalRole.ifPresent(roles::add));
        }
        user.setRoles(roles);
        return UserMapper.toUserDtoRpc(userRepository.save(user));
    }

    @Override
    public UserDtoRpc updateUser(String name, String email, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setEmail(email);
            user.setUsername(username);
            return UserMapper.toUserDtoRpc(userRepository.save(user));
        }
        return null;
    }

    @Override
    public boolean existUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
