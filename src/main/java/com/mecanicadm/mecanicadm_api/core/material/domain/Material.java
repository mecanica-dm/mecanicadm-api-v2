package com.mecanicadm.mecanicadm_api.core.material.domain;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Material {

    private UUID id;

    private String name;

    private String brand;

    private String description;

    private BigDecimal price;

    private MaterialType type;

    protected Material() {
    }

    private Material(UUID id, String name, String brand, String description, BigDecimal price, MaterialType type) {
        this.id = id;
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
    }

    public static Material create(String name, String brand, String description, BigDecimal price, MaterialType type) {
        return new Material(null, name, brand, description, price, type);
    }

    public void update(String name, String brand, String description, BigDecimal price, MaterialType type) {
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
    }

    public static Material restore(UUID id, String name, String brand, String description, BigDecimal price, MaterialType type) {
        return new Material(id, name, brand, description, price, type);
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
