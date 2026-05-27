package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborsUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.SearchLaborsQuery;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllLaborsService implements GetAllLaborsUseCase {

    private final LaborRepository laborRepository;

    public GetAllLaborsService(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    public Page<LaborResponse> handle(SearchLaborsQuery query) {
        Specification<Labor> spec = (root, q, cb) -> {
            HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query.name())) {
                predicates.add(hcb.ilike(root.get("name"), "%" + query.name() + "%"));
            }

            return hcb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Labor> page = laborRepository.findAll(spec, query.pageable());
        return page.map(LaborResponse::new);
    }
}
