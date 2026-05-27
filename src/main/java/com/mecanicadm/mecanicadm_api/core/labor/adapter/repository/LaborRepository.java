package com.mecanicadm.mecanicadm_api.core.labor.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LaborRepository extends JpaRepository<Labor, UUID>, JpaSpecificationExecutor<Labor> {

    @Query("SELECT l FROM Labor l WHERE l.id IN :ids")
    List<Labor> findAllByIds(@Param("ids") Set<UUID> ids);
}
