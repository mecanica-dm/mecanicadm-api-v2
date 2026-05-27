package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetAllWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrdersQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
public class GetAllWorkOrderService implements GetAllWorkOrderUseCase {

    private final WorkOrderRepository repository;

    public GetAllWorkOrderService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkOrder> handle(GetAllWorkOrdersQuery query) {
        Specification<WorkOrder> spec = Specification.where(null);

        if (Objects.nonNull(query.clientId())) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("clientId"), query.clientId()));
        }

        if (isNotBlank(query.licensePlate())) {
            spec = spec.and((root, q, cb) ->
                    cb.like(root.get("vehicleId"), "%" + query.licensePlate() + "%"));
        }

        return repository.findAll(spec, query.pageable());
    }
}
