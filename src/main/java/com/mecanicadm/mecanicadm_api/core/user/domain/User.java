package com.mecanicadm.mecanicadm_api.core.user.domain;

import com.mecanicadm.mecanicadm_api.core.user.domain.enums.UserRole;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends AuditEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private final Set<UserRole> roles = new HashSet<>();

    protected User() {
    }

    protected User(String email, String encodedPassword, String name) {
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

    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
        validate();
    }

    public void changePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        validatePassword(rawPassword);
        this.password = passwordEncoder.encode(rawPassword);
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

    public Set<UserRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void addRole(UserRole role) {
        this.roles.add(role);
    }

    public void removeRole(UserRole role) {
        this.roles.remove(role);
    }

    private void validate() {
        if (!StringUtils.hasText(this.email)) {
            throw new UserExceptions.EmailNotEmpty();
        }
    }

    public boolean isDeleted() {
        return super.deletedAt != null;
    }
}