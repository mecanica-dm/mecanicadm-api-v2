package com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.api;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.api.openapi.StockMovementsOpenApi;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.GetStockStatementUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto.StockStatementDTO;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.query.GetStockStatementQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/stock-movements")
public class StockMovementsController implements StockMovementsOpenApi {

    private final GetStockStatementUseCase getStockStatementUseCase;

    public StockMovementsController(GetStockStatementUseCase getStockStatementUseCase) {
        this.getStockStatementUseCase = getStockStatementUseCase;
    }

    @Override
    @GetMapping("/{materialId}/statement")
    public ResponseEntity<StockStatementDTO> getStatement(@PathVariable UUID materialId) {
        StockStatementDTO statement = getStockStatementUseCase.handle(new GetStockStatementQuery(materialId));
        return ResponseEntity.ok(statement);
    }
}
