package com.tracking.userservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Entity
@Table(name = "users")
@Schema(description = "Product information")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User's id", example = "1")
    private Long id;

    @Schema(description = "User's name", example = "John")
    private String name;

    @Schema(description = "User's email", example = "john@example.com")
    private String email;

    @Schema(description = "User's username", example = "john9101")
    private String username;

    @Schema(description = "User's password", example = "abc123xyz")
    private String password;

    private Boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @PrePersist
    protected void onCreate() {
        this.deleted = false;
    }
}
