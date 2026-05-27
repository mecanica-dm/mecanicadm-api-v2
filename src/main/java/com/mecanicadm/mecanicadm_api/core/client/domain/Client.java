package com.mecanicadm.mecanicadm_api.core.client.domain;

import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Entity
@Table(name = "clients")
@SQLDelete(sql = "UPDATE clients SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Client extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "document", nullable = false, unique = true)
    private String document;

    @Column(name = "phone")
    private String phone;

    protected Client() {
    }

    private Client(String name, String email, String document, String phone) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.phone = phone;
        validate();
    }

    public static Client create(String name, String email, String document, String phone) {
        return new Client(name, email, document, phone);
    }

    public void updateInfo(String name, String email, String document, String phone) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.phone = phone;
        validate();
    }

    private void validate() {
        if (!StringUtils.hasText(this.name)) {
            throw new ClientExceptions.NameNotEmpty();
        }
        if (!StringUtils.hasText(this.email)) {
            throw new ClientExceptions.EmailNotEmpty();
        }
        if (!StringUtils.hasText(this.document)) {
            throw new ClientExceptions.DocumentNotEmpty();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }

    public String getPhone() {
        return phone;
    }
}
