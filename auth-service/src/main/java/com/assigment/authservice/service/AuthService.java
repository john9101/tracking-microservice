package com.assigment.authservice.service;

import com.assigment.authservice.dto.request.LoginRequest;
import com.assigment.authservice.dto.request.RegisterRequest;
import com.assigment.authservice.dto.response.LoggedInResponse;
import com.assigment.authservice.dto.response.RegisteredResponse;
import com.nimbusds.jose.JOSEException;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    RegisteredResponse register(RegisterRequest request);

    LoggedInResponse login(LoginRequest request);
}
