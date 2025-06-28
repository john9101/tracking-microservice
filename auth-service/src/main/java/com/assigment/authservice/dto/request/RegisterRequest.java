package com.assigment.authservice.dto.request;

import com.assigment.authservice.annotation.AdvanceRequestValidation;
import com.assigment.authservice.util.ConstantUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AdvanceRequestValidation
public class RegisterRequest {
    @NotBlank(message = ConstantUtil.FIELD_NAME_NOT_BLANK )
    private String name;

    @NotBlank(message = ConstantUtil.FIELD_USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = ConstantUtil.FIELD_EMAIL_NOT_BLANK)
    @Email(message = ConstantUtil.FIELD_EMAIL_INVALID_FORMAT)
    private String email;

    @NotBlank(message = ConstantUtil.FIELD_PASSWORD_NOT_BLANK)
    @Size(min = 6, max = 100, message = ConstantUtil.FIELD_PASSWORD_SIZE)
    private String password;
}
