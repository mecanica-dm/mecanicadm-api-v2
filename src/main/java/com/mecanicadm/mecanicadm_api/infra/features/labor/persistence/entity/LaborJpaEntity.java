package com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.entity;

import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "labors")
@SQLDelete(sql = "UPDATE labors SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class LaborJpaEntity extends AuditEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public LaborJpaEntity() {
    }

    public LaborJpaEntity(UUID id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

