package com.mecanicadm.mecanicadm_api.shared.domain;

import java.time.LocalDateTime;

public abstract class AuditDomain {

    protected LocalDateTime dateCreated;
    protected LocalDateTime dateUpdated;
    protected LocalDateTime deletedAt;

    protected AuditDomain() {
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void create() {
        LocalDateTime now = LocalDateTime.now();
        this.dateCreated = now;
        this.dateUpdated = now;
    }

    public void update() {
        this.dateUpdated = LocalDateTime.now();
    }

    public void delete() {
        LocalDateTime now = LocalDateTime.now();
        this.deletedAt = now;
        this.dateUpdated = now;
    }
}
