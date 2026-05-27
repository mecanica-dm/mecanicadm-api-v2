package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkOrderMaterialItemRepository extends JpaRepository<WorkOrderLaborItem, UUID> {
}
