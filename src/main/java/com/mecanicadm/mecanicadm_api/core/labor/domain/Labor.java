package com.mecanicadm.mecanicadm_api.core.labor.domain;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Labor {

    private UUID id;
    private String name;
    private BigDecimal price;

    private Labor(UUID id, String name, BigDecimal price) {
        this.id = id;
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    private Labor(String name, BigDecimal price) {
        this(null, name, price);
    }

    public static Labor create(String name, BigDecimal price) {
        return new Labor(name, price);
    }

    public static Labor restore(UUID id, String name, BigDecimal price) {
        return new Labor(id, name, price);
    }

    public void update(String name, BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
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
