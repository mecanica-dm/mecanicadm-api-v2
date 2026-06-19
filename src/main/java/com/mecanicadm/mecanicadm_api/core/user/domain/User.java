package com.mecanicadm.mecanicadm_api.core.user.domain;

import com.mecanicadm.mecanicadm_api.core.user.domain.enums.UserRole;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.shared.domain.AuditDomain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User extends AuditDomain {

    private final UUID id;
    private String email;
    private String password;
    private String name;
    private List<UserRole> roles = new ArrayList<>();

    private User(UUID id, String email, String password, String name, List<UserRole> roles, LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
        this.deletedAt = deletedAt;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        validate();
    }

    protected User(String email, String encodedPassword, String name) {
        super();
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = encodedPassword;
        this.name = name;
        this.roles.add(UserRole.USER);
        validate();
    }

    public static User create(String email, String rawPassword, String name, PasswordEncoder passwordEncoder) {
        validatePassword(rawPassword);
        return new User(email, passwordEncoder.encode(rawPassword), name);
    }

    public static User restore(UUID id, String email, String password, String name, List<UserRole> roles, LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        return new User(id, email, password, name, roles, deletedAt, dateCreated, dateUpdated);
    }

    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
        this.dateUpdated = LocalDateTime.now();
        validate();
    }

    public void changePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        validatePassword(rawPassword);
        this.password = passwordEncoder.encode(rawPassword);
        this.dateUpdated = LocalDateTime.now();
    }

    private static void validatePassword(String rawPassword) {
        if (!StringUtils.hasText(rawPassword) || rawPassword.length() < 6) {
            throw new UserExceptions.PasswordMinLength();
        }
    }

    public void verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new UserExceptions.BadCredentials();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void addRole(UserRole role) {
        this.roles.add(role);
        this.dateUpdated = LocalDateTime.now();
    }

    public void removeRole(UserRole role) {
        this.roles.remove(role);
        this.dateUpdated = LocalDateTime.now();
    }

    private void validate() {
        if (!StringUtils.hasText(this.email)) {
            throw new UserExceptions.EmailNotEmpty();
        }
    }

}
