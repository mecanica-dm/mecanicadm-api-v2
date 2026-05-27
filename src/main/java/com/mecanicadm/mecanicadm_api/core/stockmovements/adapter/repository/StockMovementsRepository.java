package com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockMovementsRepository extends JpaRepository<StockMovements, UUID> {

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN sm.type = 'ADDITION' THEN sm.quantity
                    ELSE -sm.quantity
                END
            ), 0)
            FROM StockMovements sm
            WHERE sm.materialId = :materialId
            """)
    Integer getCurrentBalanceByMaterialId(@Param("materialId") UUID materialId);

    Optional<StockMovements> findByMaterialId(UUID materialId);

    Optional<StockMovements> findByMaterialIdAndWorkOrderId(UUID materialId, UUID workOrderId);

    List<StockMovements> findAllByMaterialIdOrderByDateCreatedDesc(UUID materialId);
}
