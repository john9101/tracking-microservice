package com.tracking.userservice.controller;

import com.tracking.userservice.dto.request.CreateUserRequest;
import com.tracking.userservice.dto.request.UpdateUserRequest;
import com.tracking.userservice.dto.response.*;
import com.tracking.userservice.entity.User;
import com.tracking.userservice.service.UserService;
import com.tracking.userservice.util.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<GotAllUsersResponse>> gotAllUsers(Locale locale) {
        GotAllUsersResponse response = userService.getAllUsers(locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_GET_ALL_SUCCESS, null, locale);
        ApiResponseWrapper<GotAllUsersResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @Operation(summary = "Get user information", description = "Get user information by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user information success",
                    content = @Content(
                            schema = @Schema(implementation = User.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Not found user")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<GotUserResponse>> getUser(@PathVariable Long id, Locale locale) {
        GotUserResponse response = userService.getUser(id, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_GET_SUCCESS, null, locale);
        ApiResponseWrapper<GotUserResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @Operation(summary = "Update user information", description = "Update user information by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user information success"),
            @ApiResponse(responseCode = "404", description = "Not found user")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<UpdatedUserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request, Locale locale) {
        UpdatedUserResponse response = userService.updateUser(id, request, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_SUCCESS, null, locale);
        ApiResponseWrapper<UpdatedUserResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<CreatedUserResponse>> createUser(@Valid @RequestBody CreateUserRequest request, Locale locale) {
        CreatedUserResponse response = userService.createUser(request, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_CREATE_SUCCESS, null, locale);
        ApiResponseWrapper<CreatedUserResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<DeletedUserResponse>> deleteUser(@PathVariable Long id, Locale locale) {
        DeletedUserResponse response = userService.deleteUser(id, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_DELETE_SUCCESS, null, locale);
        ApiResponseWrapper<DeletedUserResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }
}
