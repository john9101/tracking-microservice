package com.tracking.userservice.dto.request;

import com.tracking.userservice.annotation.AdvanceRequestValidation;
import com.tracking.userservice.util.ConstantUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AdvanceRequestValidation
public class CreateUserRequest {
    @NotBlank(message = ConstantUtil.FIELD_USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = ConstantUtil.FIELD_NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = ConstantUtil.FIELD_EMAIL_NOT_BLANK)
    @Email(message = ConstantUtil.FIELD_EMAIL_INVALID_FORMAT)
    private String email;

    @NotBlank(message = ConstantUtil.FIELD_PASSWORD_NOT_BLANK)
    @Size(min = 6, max = 100, message = ConstantUtil.FIELD_PASSWORD_SIZE)
    private String password;

    @NotEmpty(message = ConstantUtil.FIELD_ROLE_NAMES_NOT_EMPTY)
    private List<String> roleNames;
}
