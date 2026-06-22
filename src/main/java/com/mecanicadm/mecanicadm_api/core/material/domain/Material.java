package com.mecanicadm.mecanicadm_api.core.material.domain;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.shared.domain.AuditDomain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Material extends AuditDomain {

    private UUID id;

    private String name;

    private String brand;

    private String description;

    private BigDecimal price;

    private MaterialType type;

    private Material(UUID id, String name, String brand, String description, BigDecimal price, MaterialType type,
                     LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
        this.deletedAt = deletedAt;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public static Material create(String name, String brand, String description, BigDecimal price, MaterialType type) {
        var material = new Material(null, name, brand, description, price, type, null, null, null);
        material.create();
        return material;
    }

    public void update(String name, String brand, String description, BigDecimal price, MaterialType type) {
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
        update();
    }

    public static Material restore(UUID id, String name, String brand, String description, BigDecimal price, MaterialType type,
                                    LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        return new Material(id, name, brand, description, price, type, deletedAt, dateCreated, dateUpdated);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MaterialType getType() {
        return type;
    }
}
