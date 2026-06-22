package com.mecanicadm.mecanicadm_api.core.labor.domain;

import com.mecanicadm.mecanicadm_api.shared.domain.AuditDomain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Labor extends AuditDomain {

    private UUID id;
    private String name;
    private BigDecimal price;

    private Labor(UUID id, String name, BigDecimal price, LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
        this.deletedAt = deletedAt;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    private Labor(String name, BigDecimal price) {
        this(null, name, price, null, null, null);
    }

    public static Labor create(String name, BigDecimal price) {
        var labor = new Labor(name, price);
        labor.create();
        return labor;
    }

    public static Labor restore(UUID id, String name, BigDecimal price, LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        return new Labor(id, name, price, deletedAt, dateCreated, dateUpdated);
    }

    public void update(String name, BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
        update();
    }

    public void softDelete() {
        delete();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
