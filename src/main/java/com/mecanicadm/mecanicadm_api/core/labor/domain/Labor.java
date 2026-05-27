package com.mecanicadm.mecanicadm_api.core.labor.domain;

import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "labors")
@SQLDelete(sql = "UPDATE labors SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Labor extends AuditEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Labor() {
    }

    private Labor(String name, BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    public static Labor create(String name, BigDecimal price) {
        return new Labor(name, price);
    }

    public void update(String name, BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
