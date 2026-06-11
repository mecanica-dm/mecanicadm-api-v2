package com.mecanicadm.mecanicadm_api.infra.baseentities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
public abstract class AuditEntity {

    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    protected LocalDateTime dateCreated;

    @Column(name = "date_updated")
    @UpdateTimestamp
    protected LocalDateTime dateUpdated;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
