package com.mecanicadm.mecanicadm_api.core.client.domain;

import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class Client {

    private UUID id;

    private String name;

    private String email;

    private String document;

    private String phone;

    protected Client() {
    }

    public Client(UUID id, String name, String email, String document, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.document = document;
        this.phone = phone;
        validate();
    }

    public static Client create(String name, String email, String document, String phone) {
        return new Client(null, name, email, document, phone);
    }

    public void update(String name, String email, String document, String phone) {
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
