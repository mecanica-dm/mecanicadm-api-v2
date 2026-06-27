package com.mecanicadm.mecanicadm_api.shared.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AuditDomain {

    protected LocalDateTime dateCreated;
    protected LocalDateTime dateUpdated;
    protected LocalDateTime deletedAt;

    public AuditDomain() {
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
        this.dateUpdated = LocalDateTime.now();
    }
}
