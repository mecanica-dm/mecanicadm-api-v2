package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetAllMaterialsUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.SearchMaterialsQuery;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllMaterialsService implements GetAllMaterialsUseCase {

    private final MaterialRepository repository;

    public GetAllMaterialsService(MaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialResponse> handle(SearchMaterialsQuery query) {
        Specification<Material> spec = (root, q, cb) -> {
            HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query.name())) {
                predicates.add(hcb.ilike(root.get("name"), "%" + query.name() + "%"));
            }

            if (StringUtils.hasText(query.brand())) {
                predicates.add(hcb.ilike(root.get("brand"), "%" + query.brand() + "%"));
            }

            if (query.type() != null) {
                predicates.add(hcb.equal(root.get("type"), query.type()));
            }

            return hcb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Material> page = repository.findAll(spec, query.pageable());
        return page.map(MaterialResponse::new);
    }
}
