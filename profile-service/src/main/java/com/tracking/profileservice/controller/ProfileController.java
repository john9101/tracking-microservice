package com.tracking.profileservice.controller;

import com.tracking.profileservice.dto.request.UpdateProfileRequest;
import com.tracking.profileservice.dto.response.ApiResponseWrapper;
import com.tracking.profileservice.dto.response.GotProfileResponse;
import com.tracking.profileservice.dto.response.UpdatedProfileResponse;
import com.tracking.profileservice.service.ProfileService;
import com.tracking.profileservice.util.ConstantUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin("*")
@RequestMapping("/profiles")
@RequiredArgsConstructor
@RestController
@Tag(name = "Profile API")
public class ProfileController {
    private final ProfileService profileService;

    private final MessageSource messageSource;

    @GetMapping("/current")
    public ResponseEntity<ApiResponseWrapper<GotProfileResponse>> getProfile(
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        GotProfileResponse response = profileService.getProfile(username, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_GET_SUCCESS, null, locale);
        ApiResponseWrapper<GotProfileResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @PatchMapping("/current")
    public ResponseEntity<ApiResponseWrapper<UpdatedProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        UpdatedProfileResponse response = profileService.updateProfile(username, locale, request);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_SUCCESS, null, locale);
        ApiResponseWrapper<UpdatedProfileResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    private String getPrincipal(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("X-Username");
    }
}
