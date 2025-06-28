package com.tracking.profileservice.dto.request;

import com.tracking.profileservice.util.ConstantUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileRequest {
    @NotBlank(message = ConstantUtil.FIELD_NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = ConstantUtil.FIELD_EMAIL_NOT_BLANK)
    @Email(message = ConstantUtil.FIELD_EMAIL_INVALID_FORMAT)
    private String email;
}
