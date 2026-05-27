package com.mecanicadm.mecanicadm_api.core.client.adapter.repository.specification;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ClientSpecification {

    private ClientSpecification() {
    }

    public static Specification<Client> hasName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasDocument(String document) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(document)) return null;
            return cb.equal(root.get("document"), document);
        };
    }
}
