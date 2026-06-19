package com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;
import com.mecanicadm.mecanicadm_api.infra.features.stockmovements.persistence.entity.StockMovementsJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.stockmovements.persistence.jpa.StockMovementsJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class StockMovementsJpaRepositoryTest {

    @Autowired
    private StockMovementsJpaRepository repository;

    @Test
    @DisplayName("Deve buscar todas as movimentacoes de um material ordenadas por data descrescente")
    @Sql(scripts = "/sql/stock_movements_data.sql")
    void shouldFindAllMovementsOrderedByDateDesc() {
        UUID materialId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        List<StockMovementsJpaEntity> results = repository.findAllByMaterialIdOrderByDateCreatedDesc(materialId);

        assertEquals(2, results.size());
        assertEquals(MovementType.REDUCTION, results.get(0).getType());
        assertEquals(MovementType.ADDITION, results.get(1).getType());
    }

    @Test
    @DisplayName("Deve calcular o saldo atual do material")
    @Sql(scripts = "/sql/stock_movements_data.sql")
    void shouldCalculateCurrentBalance() {
        UUID materialId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        Integer balance = repository.getCurrentBalanceByMaterialId(materialId);

        assertEquals(7, balance);
    }
}
