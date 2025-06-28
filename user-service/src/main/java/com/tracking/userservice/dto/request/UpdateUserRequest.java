package com.tracking.userservice.dto.request;


import com.tracking.userservice.util.ConstantUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserRequest {
    @NotBlank(message = ConstantUtil.FIELD_EMAIL_NOT_BLANK)
    private String name;

    @NotBlank(message = ConstantUtil.FIELD_EMAIL_NOT_BLANK)
    @Email(message = ConstantUtil.FIELD_EMAIL_INVALID_FORMAT)
    private String email;
}
