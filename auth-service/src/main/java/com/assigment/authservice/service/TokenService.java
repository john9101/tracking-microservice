package com.assigment.authservice.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(UserDetails userDetails);
}
