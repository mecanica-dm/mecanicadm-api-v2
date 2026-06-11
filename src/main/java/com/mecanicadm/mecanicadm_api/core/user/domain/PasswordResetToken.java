package com.mecanicadm.mecanicadm_api.core.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetToken {

    private UUID id;
    private String token;
    private User user;
    private LocalDateTime expiryDate;

    public PasswordResetToken(UUID id, String token, User user, LocalDateTime expiryDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public PasswordResetToken(String token, User user, LocalDateTime expiryDate) {
        this(null, token, user, expiryDate);
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
