package com.tracking.profileservice.service;

import com.tracking.profileservice.dto.request.UpdateProfileRequest;
import com.tracking.profileservice.dto.response.GotProfileResponse;
import com.tracking.profileservice.dto.response.UpdatedProfileResponse;

import java.util.Locale;

public interface ProfileService {
    GotProfileResponse getProfile(String username, Locale locale);

    UpdatedProfileResponse updateProfile(String username, Locale locale, UpdateProfileRequest request);
}
