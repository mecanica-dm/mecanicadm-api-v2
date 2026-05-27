package com.mecanicadm.mecanicadm_api.core.material.domain;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "materials")
@SQLDelete(sql = "UPDATE materials SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Material extends AuditEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MaterialType type;

    protected Material() {
    }

    private Material(String name, String brand, String description, BigDecimal price, MaterialType type) {
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
    }

    public static Material create(String name, String brand, String description, BigDecimal price, MaterialType type) {
        return new Material(name, brand, description, price, type);
    }

    public void update(String name, String brand, String description, BigDecimal price, MaterialType type) {
        this.name = requireNonNull(name);
        this.brand = brand;
        this.description = description;
        this.price = requireNonNull(price);
        this.type = requireNonNull(type);
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
