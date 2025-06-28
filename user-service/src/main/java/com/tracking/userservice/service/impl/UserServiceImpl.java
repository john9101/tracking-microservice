package com.tracking.userservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.userservice.dto.request.CreateUserRequest;
import com.tracking.userservice.dto.request.UpdateUserRequest;
import com.tracking.userservice.dto.response.*;
import com.tracking.userservice.entity.Role;
import com.tracking.userservice.entity.User;
import com.tracking.userservice.exception.UserServiceException;
import com.tracking.userservice.repository.RoleRepository;
import com.tracking.userservice.repository.UserRepository;
import com.tracking.userservice.service.UserService;
import com.tracking.userservice.util.ConstantUtil;
import com.tracking.userservice.util.PasswordUtil;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final MeterRegistry meterRegistry;
    private DistributionSummary distributionSummary;
    private final ObjectMapper objectMapper;

    @Override
    public GotUserResponse getUser(Long id, Locale locale) {
        User user = manifestLifeUser(id, locale);
        return GotUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    @Override
    public UpdatedUserResponse updateUser(Long id, UpdateUserRequest updateUser, Locale locale) {
        try {
            User user = manifestLifeUser(id, locale);
            user.setName(updateUser.getName());
            user.setEmail(updateUser.getEmail());
            user = userRepository.save(user);
            return UpdatedUserResponse.builder().id(user.getId()).build();
        } catch (Exception e) {
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_FAIL, null, locale);
            throw new UserServiceException(failMessage, e);
        }
    }

    @Override
    public CreatedUserResponse createUser(CreateUserRequest request, Locale locale) {

        try {
            distributionSummary = DistributionSummary.builder("user.create.summary")
                    .description("A create user summary").register(meterRegistry);
            User user = new User();
            user.setName(request.getName());
            user.setPassword(PasswordUtil.encodePassword(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            List<Role> roles = roleRepository.findByNameIn(request.getRoleNames());
            if (roles.isEmpty()) {
                List.of(roleRepository.findByName(ConstantUtil.ROLE_USER))
                        .forEach(optionalRole -> optionalRole.ifPresent(roles::add));
            }
            user.setRoles(roles);
            user = userRepository.save(user);
            CreatedUserResponse response = CreatedUserResponse.builder().id(user.getId()).build();
            byte[] responseBytes = objectMapper.writeValueAsBytes(response);
            distributionSummary.record(ByteBuffer.wrap(responseBytes).getInt());
            return response;
        } catch (Exception e) {
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_CREATE_FAIL, null, locale);
            throw new UserServiceException(failMessage, e);
        }
    }

    @Override
    public DeletedUserResponse deleteUser(Long id, Locale locale) {
        try {
            User user = manifestLifeUser(id, locale);
            user.setDeleted(true);
            user = userRepository.save(user);
            return DeletedUserResponse.builder().id(user.getId()).build();
        } catch (Exception e) {
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_DELETE_FAIL, null, locale);
            throw new UserServiceException(failMessage, e);
        }
    }

    @Override
    public GotAllUsersResponse getAllUsers(Locale locale) {
        List<User> users = userRepository.findAllByDeleted(false);
        List<GotAllUsersResponse.Item> items = users.stream().map(user ->
                GotAllUsersResponse.Item.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
        ).toList();
        return GotAllUsersResponse.builder().items(items).build();
    }

    private User manifestLifeUser(Long id, Locale locale) {
        return userRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage(ConstantUtil.CONSTRAINT_ID_NOT_FOUND, new Object[]{id}, locale)
        ));
    }
}
