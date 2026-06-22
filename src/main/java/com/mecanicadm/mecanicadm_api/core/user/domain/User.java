package com.mecanicadm.mecanicadm_api.core.user.domain;

import com.mecanicadm.mecanicadm_api.core.user.domain.enums.UserRole;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.shared.domain.AuditDomain;

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

    public static User create(String email, String encodedPassword, String name) {
        var user = new User(email, encodedPassword, name);
        user.create();
        return user;
    }

    public static User restore(UUID id, String email, String password, String name, List<UserRole> roles, LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        return new User(id, email, password, name, roles, deletedAt, dateCreated, dateUpdated);
    }

    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
        update();
        validate();
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
        update();
    }

    public static void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank() || rawPassword.length() < 6) {
            throw new UserExceptions.PasswordMinLength();
        }
    }

    public void softDelete() {
        delete();
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
        update();
    }

    public void removeRole(UserRole role) {
        this.roles.remove(role);
        update();
    }

    private void validate() {
        if (this.email == null || this.email.isBlank()) {
            throw new UserExceptions.EmailNotEmpty();
        }
    }

}
