package com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa.specification;

import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientFilter;
import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.entity.ClientJpaEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ClientSpecificationBuilder {

    private ClientSpecificationBuilder() {
    }

    public static Specification<ClientJpaEntity> buildFilterSpecification(ClientFilter filter) {
        return (root, q, cb) -> {
            if (isNull(filter)) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            addNameFilter(predicates, filter, root, cb);
            addDocumentFilter(predicates, filter, root, cb);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static void addNameFilter(List<Predicate> predicates, ClientFilter filter, Root<ClientJpaEntity> root, CriteriaBuilder cb) {
            if (StringUtils.hasText(filter.name())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
            }
    }

    public static void addDocumentFilter(List<Predicate> predicates, ClientFilter filter, Root<ClientJpaEntity> root, CriteriaBuilder cb) {
        if (StringUtils.hasText(filter.document())) {
            predicates.add(cb.equal(root.get("document"), filter.document()));
        }
    }
}
