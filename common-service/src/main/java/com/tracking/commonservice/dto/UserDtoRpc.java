package com.tracking.commonservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter @Setter
public class UserDtoRpc implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}
